package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CreatePaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.TransactionReceiptResponse;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ReceiptPdfService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = {
	    "http://localhost:5173",
	    "https://realestatee-two.vercel.app"
	})
public class PaymentController {

    private final PaymentService paymentService;
    private final ReceiptPdfService receiptPdfService;

    public PaymentController(
            PaymentService paymentService,
            ReceiptPdfService receiptPdfService) {

        this.paymentService = paymentService;
        this.receiptPdfService = receiptPdfService;
    }

    // =========================================
    // CREATE PAYMENT
    // =========================================

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody CreatePaymentRequest request,
            Authentication authentication) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                paymentService.createPayment(
                        request,
                        email
                )
        );
    }


    // =========================================
    // BUYER PAYMENT HISTORY
    // =========================================

 // =========================================
 // VERIFY CASHFREE PAYMENT
 // =========================================

 @GetMapping("/verify/{orderId}")
 public ResponseEntity<PaymentResponse> verifyPayment(
         @PathVariable String orderId,
         Authentication authentication) {

     String email =
             authentication.getName();

     return ResponseEntity.ok(
             paymentService.verifyPayment(
                     orderId,
                     email
             )
     );
 }
    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            Authentication authentication) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                paymentService.getBuyerPayments(
                        email
                )
        );
    }


    // =========================================
    // GET PAYMENT BY ID
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long id,
            Authentication authentication) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                paymentService.getPaymentById(
                        id,
                        email
                )
        );
    }
    @PostMapping("/webhook")
    public ResponseEntity<String> paymentWebhook(
            @RequestHeader("x-webhook-signature")
            String signature,

            @RequestHeader("x-webhook-timestamp")
            String timestamp,

            @RequestBody String payload) {

        paymentService.handleWebhook(
                signature,
                timestamp,
                payload
        );

        return ResponseEntity.ok("Webhook received");
    }
 // =========================================
 // TRANSACTION RECEIPT
 // =========================================

 @GetMapping("/{paymentId}/receipt")
 public ResponseEntity<TransactionReceiptResponse> getReceipt(
         @PathVariable Long paymentId,
         Authentication authentication) {

     String email =
             authentication.getName();

     return ResponseEntity.ok(
             paymentService.getTransactionReceipt(
                     paymentId,
                     email
             )
     );
 }
 @GetMapping("/{paymentId}/receipt/pdf")
 public ResponseEntity<byte[]> downloadReceipt(
         @PathVariable Long paymentId,
         Authentication authentication)
         throws Exception {

     String email =
             authentication.getName();

     TransactionReceiptResponse receipt =
             paymentService.getTransactionReceipt(
                     paymentId,
                     email
             );

     byte[] pdf =
             receiptPdfService.generateReceipt(
                     receipt
             );

     return ResponseEntity.ok()
             .header(
                     "Content-Disposition",
                     "attachment; filename=DOMLEA-Receipt-"
                             + paymentId
                             + ".pdf"
             )
             .header(
                     "Content-Type",
                     "application/pdf"
             )
             .body(pdf);
 }
 @GetMapping("/seller/bookings")
 public ResponseEntity<List<PaymentResponse>> getSellerBookings(
         Authentication authentication) {

     String email =
             authentication.getName();

     return ResponseEntity.ok(
             paymentService.getSellerBookings(email)
     );
 }
}