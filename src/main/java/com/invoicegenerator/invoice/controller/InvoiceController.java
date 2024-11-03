package com.invoicegenerator.invoice.controller;

import com.invoicegenerator.invoice.Invoice;
import com.invoicegenerator.invoice.service.InvoicePDFService;
import com.invoicegenerator.invoice.service.InvoiceService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoicePDFService invoicePDFService;

    @PostMapping
    public ResponseEntity<Invoice> create(@RequestBody Invoice invoice) throws DocumentException, IOException {
        Invoice invoiceNew = invoiceService.create(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceNew);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoice() {
        List<Invoice> Invoices = invoiceService.allInvoice();
        return ResponseEntity.ok(Invoices);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> update(
            @PathVariable("id") UUID id,
            @RequestBody Invoice updateInvoice) {
        Invoice upInvoice = invoiceService.update(id, updateInvoice);
        return ResponseEntity.ok(upInvoice);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateInvoicePDF(@PathVariable UUID id) {
        try {
            Invoice invoice = invoiceService.getInvoiceById(id);

            byte[] pdf = invoicePDFService.generateInvoicePDF(invoice);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice-" + invoice.getInvoiceNumber() + ".pdf");

            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
