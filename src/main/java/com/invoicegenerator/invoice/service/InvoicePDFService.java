package com.invoicegenerator.invoice.service;

import com.invoicegenerator.address.Address;
import com.invoicegenerator.invoice.Invoice;
import com.invoicegenerator.invoice.InvoiceItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.format.DateTimeFormatter;

@Service
public class InvoicePDFService {

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public byte[] generateInvoicePDF(Invoice invoice) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        addHeader(document, invoice);
        addDateAndInvoiceNumber(document, invoice);
        addRecipientDetails(document, invoice);
        addEmitterDetails(document, invoice);
        addItemsTable(document, invoice);
        addSummaryTable(document, invoice);
        addCommentsSection(document, invoice);
        addCenteredRecipientDetails(document, invoice);
        addFooter(document);

        document.close();
        return baos.toByteArray();
    }

    private void addHeader(Document document, Invoice invoice) throws DocumentException {
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("INVOICE", titleFont);
        title.setAlignment(Element.ALIGN_LEFT);
        document.add(title);
    }

    private void addDateAndInvoiceNumber(Document document, Invoice invoice) throws DocumentException {
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(30);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setSpacingBefore(10f);

        float[] columnWidths = {1f, 1.5f};
        table.setWidths(columnWidths);

        PdfPCell dateLabelCell = new PdfPCell(new Phrase("DATE", labelFont));
        dateLabelCell.setBorder(Rectangle.NO_BORDER);
        dateLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(dateLabelCell);

        PdfPCell dateValueCell = new PdfPCell(new Phrase(invoice.getDataCriacao().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")), valueFont));
        dateValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        dateValueCell.setBorder(Rectangle.BOX);
        table.addCell(dateValueCell);

        PdfPCell invoiceLabelCell = new PdfPCell(new Phrase("INVOICE #", labelFont));
        invoiceLabelCell.setBorder(Rectangle.NO_BORDER);
        invoiceLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(invoiceLabelCell);

        PdfPCell invoiceValueCell = new PdfPCell(new Phrase(invoice.getInvoiceNumber(), valueFont));
        invoiceValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceValueCell.setBorder(Rectangle.BOX);
        table.addCell(invoiceValueCell);

        document.add(table);
    }

    private void addRecipientDetails(Document document, Invoice invoice) throws DocumentException {
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        document.add(new Paragraph(invoice.getRecipientCompany().getName(), normalFont));

        Address recipientAddress = invoice.getRecipientCompany().getAddress();
        if (recipientAddress != null) {
            document.add(new Paragraph(recipientAddress.getAddressOne(), normalFont));
            if (recipientAddress.getAddressTwo() != null) {
                document.add(new Paragraph(recipientAddress.getAddressTwo(), normalFont));
            }
            document.add(new Paragraph(
                    String.format("%s, %s %s, %s",
                            recipientAddress.getCity(),
                            recipientAddress.getState(),
                            recipientAddress.getPostCode(),
                            recipientAddress.getCountry()),
                    normalFont
            ));
        }

        document.add(new Paragraph(invoice.getRecipientCompany().getPhoneNumber(), normalFont));
        document.add(new Paragraph(invoice.getRecipientCompany().getEmail(), normalFont));
        document.add(new Paragraph("\n"));
    }

    private void addEmitterDetails(Document document, Invoice invoice) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        PdfPTable billToTable = new PdfPTable(1);
        billToTable.setWidthPercentage(40);
        billToTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        BaseColor customBlue = new BaseColor(60, 76, 132);

        PdfPCell billToCell = new PdfPCell(new Phrase("BILL TO:", sectionFont));
        billToCell.setBackgroundColor(customBlue);
        billToCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        billToCell.setPadding(5f);
        billToTable.addCell(billToCell);

        document.add(billToTable);

        document.add(new Paragraph(invoice.getEmitterCompany().getName(), normalFont));

        Address emitterAddress = invoice.getEmitterCompany().getAddress();
        if (emitterAddress != null) {
            document.add(new Paragraph(emitterAddress.getAddressOne(), normalFont));
            if (emitterAddress.getAddressTwo() != null) {
                document.add(new Paragraph(emitterAddress.getAddressTwo(), normalFont));
            }
            document.add(new Paragraph(
                    String.format("%s, %s %s, %s",
                            emitterAddress.getCity(),
                            emitterAddress.getState(),
                            emitterAddress.getPostCode(),
                            emitterAddress.getCountry()),
                    normalFont
            ));
        }

        document.add(new Paragraph(invoice.getEmitterCompany().getPhoneNumber(), normalFont));
        document.add(new Paragraph("\n"));
    }

    private void addItemsTable(Document document, Invoice invoice) throws DocumentException {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        BaseColor customBlue = new BaseColor(60, 76, 132);

        PdfPCell descriptionHeader = new PdfPCell(new Phrase("DESCRIPTION", headerFont));
        descriptionHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        descriptionHeader.setBackgroundColor(customBlue);
        descriptionHeader.setPadding(5f);
        table.addCell(descriptionHeader);

        PdfPCell amountHeader = new PdfPCell(new Phrase("AMOUNT", headerFont));
        amountHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        amountHeader.setBackgroundColor(customBlue);
        amountHeader.setPadding(5f);
        table.addCell(amountHeader);

        for (InvoiceItem item : invoice.getItems()) {
            PdfPCell descriptionCell = new PdfPCell(new Phrase(item.getDescription(), normalFont));
            descriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(descriptionCell);

            PdfPCell amountCell = new PdfPCell(new Phrase(currencyFormat.format(item.getUnitPrice()), normalFont));
            amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(amountCell);
        }

        document.add(table);
    }

    private void addSummaryTable(Document document, Invoice invoice) throws DocumentException {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(30);
        summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.setSpacingBefore(10f);

        float[] columnWidths = {1f, 1f};
        summaryTable.setWidths(columnWidths);

        float cellPadding = 4f;

        PdfPCell subtotalLabel = new PdfPCell(new Phrase("SUBTOTAL", headerFont));
        PdfPCell subtotalValue = new PdfPCell(new Phrase(currencyFormat.format(invoice.getSubtotal()), normalFont));
        subtotalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subtotalLabel.setPadding(cellPadding);
        subtotalValue.setPadding(cellPadding);

        PdfPCell taxRateLabel = new PdfPCell(new Phrase("TAX RATE", headerFont));
        PdfPCell taxRateValue = new PdfPCell(new Phrase(invoice.getTaxRate() + "%", normalFont));
        taxRateValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        taxRateLabel.setPadding(cellPadding);
        taxRateValue.setPadding(cellPadding);

        PdfPCell taxLabel = new PdfPCell(new Phrase("TAX", headerFont));
        PdfPCell taxValue = new PdfPCell(new Phrase(currencyFormat.format(invoice.getTaxAmount()), normalFont));
        taxValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        taxLabel.setPadding(cellPadding);
        taxValue.setPadding(cellPadding);

        PdfPCell otherLabel = new PdfPCell(new Phrase("OTHER", headerFont));
        PdfPCell otherValue = new PdfPCell(new Phrase(currencyFormat.format(invoice.getAnotherValue()), normalFont));
        otherValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        otherLabel.setPadding(cellPadding);
        otherValue.setPadding(cellPadding);

        PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL", headerFont));
        PdfPCell totalValue = new PdfPCell(new Phrase(currencyFormat.format(invoice.getTotal()), totalFont));
        totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabel.setPadding(cellPadding);
        totalValue.setPadding(cellPadding);

        BaseColor lightBlue = new BaseColor(230, 240, 255);
        totalValue.setBackgroundColor(lightBlue);
        totalValue.setPadding(cellPadding);
        totalLabel.setBorderWidthTop(2f);
        totalValue.setBorderWidthTop(2f);

        summaryTable.addCell(subtotalLabel);
        summaryTable.addCell(subtotalValue);
        summaryTable.addCell(taxRateLabel);
        summaryTable.addCell(taxRateValue);
        summaryTable.addCell(taxLabel);
        summaryTable.addCell(taxValue);
        summaryTable.addCell(otherLabel);
        summaryTable.addCell(otherValue);
        summaryTable.addCell(totalLabel);
        summaryTable.addCell(totalValue);

        document.add(summaryTable);
    }

    private void addCommentsSection(Document document, Invoice invoice) throws DocumentException {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        PdfPTable commentsTable = new PdfPTable(1);
        commentsTable.setWidthPercentage(100);
        commentsTable.setSpacingBefore(10f);

        PdfPCell commentsHeader = new PdfPCell(new Phrase("COMMENTS", headerFont));
        commentsHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        commentsTable.addCell(commentsHeader);

        PdfPCell commentsContent = new PdfPCell(new Phrase(invoice.getComments(), normalFont));
        commentsContent.setFixedHeight(60);
        commentsTable.addCell(commentsContent);

        document.add(commentsTable);
    }

    private void addCenteredRecipientDetails(Document document, Invoice invoice) throws DocumentException {
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        Address recipientAddress = invoice.getRecipientCompany().getAddress();
        String addressDetails = recipientAddress != null ? String.format(
                "%s, %s, %s %s, %s",
                recipientAddress.getAddressOne(),
                recipientAddress.getCity(),
                recipientAddress.getState(),
                recipientAddress.getPostCode(),
                recipientAddress.getCountry()
        ) : "";

        Paragraph recipientDetails = new Paragraph(
                String.format("%s | %s | %s | %s",
                        invoice.getRecipientCompany().getName(),
                        addressDetails,
                        invoice.getRecipientCompany().getPhoneNumber(),
                        invoice.getRecipientCompany().getEmail()
                ),
                normalFont
        );
        recipientDetails.setAlignment(Element.ALIGN_CENTER);
        document.add(recipientDetails);
    }

    private void addFooter(Document document) throws DocumentException {
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
        Paragraph footer = new Paragraph("Thank You For Your Business!", footerFont);
        footer.setSpacingBefore(5f);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
}
