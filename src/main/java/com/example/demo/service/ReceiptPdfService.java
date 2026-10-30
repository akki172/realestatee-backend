package com.example.demo.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.example.demo.dto.TransactionReceiptResponse;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class ReceiptPdfService {

    public byte[] generateReceipt(
            TransactionReceiptResponse receipt)
            throws Exception {

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        Document document =
                new Document(
                        PageSize.A4,
                        50,
                        50,
                        50,
                        50
                );

        PdfWriter.getInstance(
                document,
                output
        );

        document.open();


        // =====================================
        // DOMLEA
        // =====================================

        Font logoFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        24,
                        Font.BOLD
                );

        Paragraph logo =
                new Paragraph(
                        "DOMLEA",
                        logoFont
                );

        logo.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(logo);


        // =====================================
        // TITLE
        // =====================================

        Font titleFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        14,
                        Font.BOLD
                );

        Paragraph title =
                new Paragraph(
                        "TRANSACTION RECEIPT",
                        titleFont
                );

        title.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(title);

        document.add(
                new Paragraph(" ")
        );


        // =====================================
        // STATUS
        // =====================================

        Paragraph status =
                new Paragraph(
                        "PAYMENT SUCCESSFUL"
                );

        status.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(status);

        document.add(
                new Paragraph(" ")
        );


        // =====================================
        // TRANSACTION DETAILS
        // =====================================

        addHeading(
                document,
                "Transaction Details"
        );

        addRow(
                document,
                "Transaction ID",
                String.valueOf(
                        receipt.getTransactionId()
                )
        );

        addRow(
                document,
                "Order ID",
                receipt.getOrderId()
        );

        addRow(
                document,
                "Payment ID",
                receipt.getPaymentId()
        );

        addRow(
                document,
                "Payment Date",
                String.valueOf(
                        receipt.getPaymentDate()
                )
        );


        // =====================================
        // BUYER DETAILS
        // =====================================

        addHeading(
                document,
                "Buyer Details"
        );

        addRow(
                document,
                "Name",
                receipt.getBuyerName()
        );

        addRow(
                document,
                "Email",
                receipt.getBuyerEmail()
        );

        addRow(
                document,
                "Phone",
                receipt.getBuyerPhone()
        );


        // =====================================
        // PROPERTY DETAILS
        // =====================================

        addHeading(
                document,
                "Property Details"
        );

        addRow(
                document,
                "Property ID",
                String.valueOf(
                        receipt.getPropertyId()
                )
        );

        addRow(
                document,
                "Property",
                receipt.getPropertyTitle()
        );

        addRow(
                document,
                "Location",
                receipt.getCity()
                        + ", "
                        + receipt.getState()
        );


        // =====================================
        // AMOUNT
        // =====================================

        document.add(
                new Paragraph(" ")
        );

        Font amountFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        16,
                        Font.BOLD
                );

        Paragraph amount =
                new Paragraph(
                        "Amount Paid: ₹"
                        + String.format(
                            "%.2f",
                            receipt.getAmount()
                        ),
                        amountFont
                );

        amount.setAlignment(
                Element.ALIGN_RIGHT
        );

        document.add(amount);


        // =====================================
        // FOOTER
        // =====================================

        document.add(
                new Paragraph(" ")
        );

        Paragraph footer =
                new Paragraph(
                        "Thank you for using DOMLEA."
                );

        footer.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(footer);


        document.close();

        return output.toByteArray();
    }


    private void addHeading(
            Document document,
            String heading)
            throws DocumentException {

        Font headingFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        12,
                        Font.BOLD
                );

        document.add(
                new Paragraph(
                        heading,
                        headingFont
                )
        );

        document.add(
                new Paragraph(" ")
        );
    }


    private void addRow(
            Document document,
            String label,
            String value)
            throws DocumentException {

        Font font =
                new Font(
                        Font.FontFamily.HELVETICA,
                        10
                );

        document.add(
                new Paragraph(
                        label + ": " + value,
                        font
                )
        );
    }
}