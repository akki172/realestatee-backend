package com.example.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;

import org.json.JSONArray;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.demo.dto.CreatePaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.TransactionReceiptResponse;
import com.example.demo.entity.ActivityType;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.entity.Property;
import com.example.demo.entity.PropertyStatus;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserActivity;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserActivityRepository;
import com.example.demo.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final PropertyRepository propertyRepository;
	private final UserRepository userRepository;
	private final NotificationService notificationService;
	private final UserActivityRepository userActivityRepository;
    @Value("${cashfree.client.id}")
    private String cashfreeClientId;

    @Value("${cashfree.client.secret}")
    private String cashfreeClientSecret;

    @Value("${cashfree.api.url}")
    private String cashfreeApiUrl;

    @Value("${cashfree.api.version}")
    private String cashfreeApiVersion;


    public PaymentService(
            PaymentRepository paymentRepository,
            PropertyRepository propertyRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            UserActivityRepository userActivityRepository) {

        this.paymentRepository = paymentRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.userActivityRepository = userActivityRepository;
    }

    // =========================================
    // CREATE PAYMENT
    // =========================================

    public PaymentResponse createPayment(
            CreatePaymentRequest request,
            String email) {

        // =====================================
        // FIND BUYER
        // =====================================

        User buyer = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );


        // =====================================
        // CHECK BUYER ROLE
        // =====================================

        if (buyer.getRole() != Role.BUYER) {

            throw new RuntimeException(
                    "Only buyers can make payments"
            );
        }


        // =====================================
        // FIND PROPERTY
        // =====================================

        Property property = propertyRepository
                .findById(request.getPropertyId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Property not found"
                        )
                );


        // =====================================
        // CHECK PROPERTY PRICE
        // =====================================

        if (property.getPrice() == null ||
            property.getPrice() <= 0) {

            throw new RuntimeException(
                    "Invalid property price"
            );
        }


        // =====================================
        // CREATE UNIQUE ORDER ID
        // =====================================

        String orderId =
                "DOMLEA_" +
                property.getId() +
                "_" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12);


        // =====================================
        // CUSTOMER ID
        // =====================================

        String customerId =
                "USER_" + buyer.getId();


        // =====================================
        // CASHFREE CLIENT
        // =====================================

        RestClient restClient =
                RestClient.builder()
                        .baseUrl(cashfreeApiUrl)
                        .build();


        // =====================================
        // CASHFREE REQUEST
        // =====================================

        String requestBody = """
        		{
        		    "order_id": "%s",
        		    "order_amount": %.2f,
        		    "order_currency": "INR",

        		    "customer_details": {
        		        "customer_id": "%s",
        		        "customer_name": "%s",
        		        "customer_email": "%s",
        		        "customer_phone": "%s"
        		    },

        		    "order_meta": {
    "return_url": "http://localhost:5173/payment/result?order_id={order_id}"
}
        		}
        		""".formatted(
        		        orderId,
        		        property.getPrice(),
        		        customerId,
        		        buyer.getFullName(),
        		        buyer.getEmail(),
        		        buyer.getPhone()
        		);
        // =====================================
        // CALL CASHFREE
        // =====================================

        String cashfreeResponse;

        try {

            cashfreeResponse =
                    restClient.post()
                            .uri("/orders")
                            .header(
                                    "x-client-id",
                                    cashfreeClientId
                            )
                            .header(
                                    "x-client-secret",
                                    cashfreeClientSecret
                            )
                            .header(
                                    "x-api-version",
                                    cashfreeApiVersion
                            )
                            .header(
                                    HttpHeaders.ACCEPT,
                                    MediaType.APPLICATION_JSON_VALUE
                            )
                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )
                            .body(requestBody)
                            .retrieve()
                            .body(String.class);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to create Cashfree payment order",
                    e
            );
        }


        // =====================================
        // READ CASHFREE RESPONSE
        // =====================================

        JSONObject json =
                new JSONObject(cashfreeResponse);


        String paymentSessionId =
                json.getString(
                        "payment_session_id"
                );


        // =====================================
        // CREATE DOMLEA PAYMENT
        // =====================================

        Payment payment =
                new Payment();


        payment.setBuyer(buyer);

        payment.setProperty(property);

        // IMPORTANT:
        // Amount comes from database.
        payment.setAmount(
                property.getPrice()
        );

        payment.setOrderId(
                orderId
        );

        payment.setPaymentSessionId(
                paymentSessionId
        );

        payment.setStatus(
                PaymentStatus.PENDING
        );


        // =====================================
        // SAVE PAYMENT
        // =====================================

        Payment savedPayment =
                paymentRepository.save(payment);


        return convertToResponse(
                savedPayment
        );
    }


    // =========================================
    // GET BUYER PAYMENTS
    // =========================================

    public List<PaymentResponse> getBuyerPayments(
            String email) {

        User buyer = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );


        return paymentRepository
                .findByBuyerId(
                        buyer.getId()
                )
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================
    // GET PAYMENT BY ID
    // =========================================

    public PaymentResponse getPaymentById(
            Long id,
            String email) {

        User buyer = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );


        Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found"
                        )
                );


        // =====================================
        // SECURITY CHECK
        // =====================================

        if (!payment.getBuyer()
                .getId()
                .equals(buyer.getId())) {

            throw new RuntimeException(
                    "You are not authorized to view this payment"
            );
        }


        return convertToResponse(
                payment
        );
    }


    // =========================================
    // ENTITY → RESPONSE
    // =========================================

    private PaymentResponse convertToResponse(
            Payment payment) {

        PaymentResponse response =
                new PaymentResponse();


        response.setId(
                payment.getId()
        );


        response.setPropertyId(
                payment.getProperty().getId()
        );


        response.setPropertyTitle(
                payment.getProperty().getTitle()
        );


        response.setAmount(
                payment.getAmount()
        );


        response.setOrderId(
                payment.getOrderId()
        );


        response.setPaymentId(
                payment.getPaymentId()
        );


        response.setPaymentSessionId(
                payment.getPaymentSessionId()
        );


        response.setStatus(
                payment.getStatus()
        );


        response.setCreatedAt(
                payment.getCreatedAt()
        );
        response.setBuyerName(
                payment.getBuyer().getFullName()
        );
        
        response.setPropertyStatus(
                payment.getProperty()
                        .getStatus()
                        .name()
        );
        
        if (payment.getProperty().getImageUrls() != null &&
        	    !payment.getProperty().getImageUrls().isEmpty()) {

        	    response.setPropertyImage(
        	        payment.getProperty()
        	               .getImageUrls()
        	               .get(0)
        	    );
        	}

        return response;
    }
 // =========================================
 // VERIFY PAYMENT
 // =========================================
 // =========================================
 // GET TRANSACTION RECEIPT
 // =========================================

 public TransactionReceiptResponse getTransactionReceipt(
         Long paymentId,
         String email) {

     // =========================================
     // FIND BUYER
     // =========================================

     User buyer = userRepository
             .findByEmail(email)
             .orElseThrow(() ->
                     new RuntimeException(
                             "User not found"
                     )
             );


     // =========================================
     // FIND PAYMENT
     // =========================================

     Payment payment =
             paymentRepository
                     .findById(paymentId)
                     .orElseThrow(() ->
                             new RuntimeException(
                                     "Payment not found"
                             )
                     );


     // =========================================
     // SECURITY CHECK
     // =========================================

     if (!payment.getBuyer()
             .getId()
             .equals(buyer.getId())) {

         throw new RuntimeException(
                 "You are not authorized to view this receipt"
         );
     }


     // =========================================
     // ONLY SUCCESSFUL PAYMENTS
     // =========================================

     if (payment.getStatus()
             != PaymentStatus.SUCCESS) {

         throw new RuntimeException(
                 "Receipt is available only for successful payments"
         );
     }


     // =========================================
     // CREATE RECEIPT RESPONSE
     // =========================================

     TransactionReceiptResponse receipt =
             new TransactionReceiptResponse();


     // =========================================
     // TRANSACTION DETAILS
     // =========================================

     receipt.setTransactionId(
             payment.getId()
     );

     receipt.setOrderId(
             payment.getOrderId()
     );

     receipt.setPaymentId(
             payment.getPaymentId()
     );

     receipt.setPaymentSessionId(
             payment.getPaymentSessionId()
     );

     receipt.setStatus(
             payment.getStatus()
     );

     receipt.setAmount(
             payment.getAmount()
     );

     receipt.setPaymentDate(
             payment.getUpdatedAt()
     );


     // =========================================
     // BUYER DETAILS
     // =========================================

     receipt.setBuyerName(
             buyer.getFullName()
     );

     receipt.setBuyerEmail(
             buyer.getEmail()
     );

     receipt.setBuyerPhone(
             buyer.getPhone()
     );


     // =========================================
     // PROPERTY DETAILS
     // =========================================

     Property property =
             payment.getProperty();

     receipt.setPropertyId(
             property.getId()
     );

     receipt.setPropertyTitle(
             property.getTitle()
     );

     receipt.setCity(
             property.getCity()
     );

     receipt.setState(
             property.getState()
     );


     return receipt;
 }
 public PaymentResponse verifyPayment(
	        String orderId,
	        String email) {

	    // =========================================
	    // FIND BUYER
	    // =========================================

	    User buyer = userRepository
	            .findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException(
	                            "User not found"
	                    )
	            );


	    // =========================================
	    // FIND PAYMENT
	    // =========================================

	    Payment payment =
	            paymentRepository
	                    .findByOrderId(orderId)
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Payment not found"
	                            )
	                    );


	    // =========================================
	    // SECURITY CHECK
	    // =========================================

	    if (!payment.getBuyer()
	            .getId()
	            .equals(buyer.getId())) {

	        throw new RuntimeException(
	                "You are not authorized to verify this payment"
	        );
	    }


	    // =========================================
	    // CASHFREE CLIENT
	    // =========================================

	    RestClient restClient =
	            RestClient.builder()
	                    .baseUrl(cashfreeApiUrl)
	                    .build();


	    // =========================================
	    // GET CASHFREE PAYMENTS
	    // =========================================

	    String response =
	            restClient.get()
	                    .uri(
	                        "/orders/"
	                        + orderId
	                        + "/payments"
	                    )
	                    .header(
	                            "x-client-id",
	                            cashfreeClientId
	                    )
	                    .header(
	                            "x-client-secret",
	                            cashfreeClientSecret
	                    )
	                    .header(
	                            "x-api-version",
	                            cashfreeApiVersion
	                    )
	                    .header(
	                            "accept",
	                            "application/json"
	                    )
	                    .retrieve()
	                    .body(String.class);


	    System.out.println(
	            "Cashfree payment response: "
	                    + response
	    );


	    // =========================================
	    // PARSE RESPONSE
	    // =========================================

	    JSONArray payments =
	            new JSONArray(response);


	    boolean success = false;

	    boolean pending = false;

	    String cashfreePaymentId = null;


	    // =========================================
	    // CHECK ALL PAYMENT ATTEMPTS
	    // =========================================

	    for (int i = 0;
	         i < payments.length();
	         i++) {

	        JSONObject paymentData =
	                payments.getJSONObject(i);


	        String paymentStatus =
	                paymentData.optString(
	                        "payment_status"
	                );


	        System.out.println(
	                "Payment attempt "
	                        + i
	                        + " status: "
	                        + paymentStatus
	        );


	        // SUCCESS
	        if ("SUCCESS".equalsIgnoreCase(
	                paymentStatus)) {

	            success = true;

	            cashfreePaymentId =
	                    paymentData.optString(
	                            "cf_payment_id"
	                    );

	            break;
	        }


	        // PENDING
	        if ("PENDING".equalsIgnoreCase(
	                paymentStatus)) {

	            pending = true;
	        }
	    }


	    // =========================================
	    // UPDATE DOMLEA PAYMENT
	    // =========================================

	    if (success) {

	        payment.setStatus(
	                PaymentStatus.SUCCESS
	        );

	        payment.setPaymentId(
	                cashfreePaymentId
	        );


	        Property property =
	                payment.getProperty();


	        if (property != null) {

	            property.setStatus(
	                    PropertyStatus.BOOKED
	            );

	            propertyRepository.save(property);


	            // =========================================
	            // PAYMENT COMPLETED ACTIVITY
	            // =========================================

	            UserActivity activity =
	                    new UserActivity();

	            activity.setUser(buyer);

	            activity.setProperty(property);

	            activity.setActivityType(
	                    ActivityType.PAYMENT_COMPLETED
	            );

	            activity.setLocation(
	                    property.getCity()
	            );

	            userActivityRepository.save(activity);


	            // =========================================
	            // SELLER NOTIFICATION
	            // =========================================

	            User seller =
	                    property.getSeller();

	            String message =
	                    "Your property \""
	                    + property.getTitle()
	                    + "\" was successfully booked by "
	                    + buyer.getFullName()
	                    + ".";

	            notificationService.createNotification(

	                    seller.getId(),

	                    message,

	                    "PROPERTY_BOOKED",

	                    property.getId(),

	                    null
	            );
	        }
	    }
	     // =========================================
	     // SELLER NOTIFICATION
	     // =========================================

	    


	    // =========================================
	    // SAVE
	    // =========================================

	    payment =
	            paymentRepository.save(payment);


	    // =========================================
	    // RETURN
	    // =========================================

	    return convertToResponse(payment);
	}
public void handleWebhook(
      String signature,
      String timestamp,
      String payload) {

  try {

      // =====================================
      // VERIFY SIGNATURE
      // =====================================

      String signedPayload =
              timestamp + payload;

      Mac mac =
              Mac.getInstance("HmacSHA256");

      SecretKeySpec secretKey =
              new SecretKeySpec(
                      cashfreeClientSecret
                              .getBytes(StandardCharsets.UTF_8),
                      "HmacSHA256"
              );

      mac.init(secretKey);

      byte[] digest =
              mac.doFinal(
                      signedPayload.getBytes(
                              StandardCharsets.UTF_8
                      )
              );

      String expectedSignature =
              Base64.getEncoder()
                      .encodeToString(digest);


      if (!expectedSignature.equals(signature)) {

          throw new RuntimeException(
                  "Invalid Cashfree webhook signature"
          );
      }


      // =====================================
      // READ WEBHOOK
      // =====================================

      JSONObject json =
              new JSONObject(payload);


      JSONObject data =
              json.getJSONObject("data");


      JSONObject order =
              data.getJSONObject("order");


      String orderId =
              order.getString("order_id");


      JSONObject payment =
              data.getJSONObject("payment");


      String paymentStatus =
              payment.getString(
                      "payment_status"
              );


      // =====================================
      // FIND DOMLEA PAYMENT
      // =====================================

      Payment domleaPayment =
              paymentRepository
                      .findByOrderId(orderId)
                      .orElseThrow(() ->
                              new RuntimeException(
                                      "Payment not found: "
                                              + orderId
                              )
                      );


      // =====================================
      // SUCCESS
      // =====================================

      if ("SUCCESS".equalsIgnoreCase(
              paymentStatus)) {

          domleaPayment.setStatus(
                  PaymentStatus.SUCCESS
          );

          domleaPayment.setPaymentId(
                  payment.optString(
                          "cf_payment_id"
                  )
          );

          paymentRepository.save(
                  domleaPayment
          );
      }


      // =====================================
      // FAILED
      // =====================================

      else if ("FAILED".equalsIgnoreCase(
              paymentStatus)) {

          domleaPayment.setStatus(
                  PaymentStatus.FAILED
          );

          paymentRepository.save(
                  domleaPayment
          );
      }

  } catch (Exception e) {

      throw new RuntimeException(
              "Webhook processing failed",
              e
      );
  }
}
public List<PaymentResponse> getSellerBookings(
        String email) {

    User seller =
            userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Seller not found"
                            )
                    );

    if (seller.getRole() != Role.SELLER) {

        throw new RuntimeException(
                "Only sellers can view bookings"
        );
    }

    return paymentRepository
            .findByPropertySellerId(seller.getId())
            .stream()
            .filter(payment ->
                    payment.getStatus()
                            == PaymentStatus.SUCCESS
            )
            .map(this::convertToResponse)
            .collect(Collectors.toList());
}
}