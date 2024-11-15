package com.invoicegenerator.controller;

import com.invoicegenerator.domain.Invoice;
import com.invoicegenerator.dto.invoice.InvoiceCreateRequest;
import com.invoicegenerator.dto.invoice.InvoiceResponse;
import com.invoicegenerator.dto.invoice.InvoiceUpdateRequest;
import com.invoicegenerator.service.InvoicePDFService;
import com.invoicegenerator.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<InvoiceResponse> create(@RequestBody InvoiceCreateRequest invoiceRequest) {
        InvoiceResponse savedInvoice = invoiceService.create(invoiceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInvoice);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoice() {
        List<InvoiceResponse> invoices = invoiceService.allInvoice();
        return ResponseEntity.ok(invoices);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();
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

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> update(@PathVariable("id") UUID id, @RequestBody InvoiceUpdateRequest invoiceRequest) {
        InvoiceResponse updatedInvoice = invoiceService.update(id, invoiceRequest);
        return ResponseEntity.ok(updatedInvoice);
    }

}
