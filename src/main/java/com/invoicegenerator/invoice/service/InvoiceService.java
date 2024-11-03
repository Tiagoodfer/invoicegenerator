package com.invoicegenerator.invoice.service;

import com.invoicegenerator.invoice.Invoice;
import com.invoicegenerator.invoice.InvoiceItem;
import com.invoicegenerator.invoice.repository.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Transactional
    public Invoice create(Invoice invoice) {

        sumInvoiceValues(invoice);

        for (InvoiceItem item : invoice.getItems()) {
            item.setInvoice(invoice);
        }
        return invoiceRepository.save(invoice);
    }

    public void delete(UUID id) {
        invoiceRepository.deleteById(id);
    }

    public List<Invoice> allInvoice() {
        return invoiceRepository.findAll();
    }

    public void sumInvoiceValues(Invoice invoice) {
        double subtotal = 0.0;

        for (InvoiceItem item : invoice.getItems()) {
            subtotal += item.getUnitPrice();
        }

        double taxAmount = subtotal * (invoice.getTaxRate() / 100.0);
        double total = subtotal - taxAmount - invoice.getAnotherValue();

        invoice.setSubtotal(subtotal);
        invoice.setTaxAmount(taxAmount);
        invoice.setTotal(total);
    }

    public Invoice update(UUID uuid, Invoice updateInvoice) {
        Invoice existingInvoice = invoiceRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        existingInvoice.setEmitterCompany(updateInvoice.getEmitterCompany());
        existingInvoice.setRecipientCompany(updateInvoice.getRecipientCompany());
        existingInvoice.setDataCriacao(updateInvoice.getDataCriacao());
        existingInvoice.setInvoiceNumber(updateInvoice.getInvoiceNumber());
        existingInvoice.setComments(updateInvoice.getComments());
        existingInvoice.setTax(updateInvoice.getTax());
        existingInvoice.setTax(updateInvoice.getTaxRate());
        existingInvoice.setAnotherValue(updateInvoice.getAnotherValue());

        existingInvoice.getItems().clear();
            for (InvoiceItem item : updateInvoice.getItems()) {
                item.setInvoice(existingInvoice);
                existingInvoice.getItems().add(item);
            }

        return invoiceRepository.save(existingInvoice);
    }

    public Invoice getInvoiceById(UUID id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isPresent()) {
            return invoice.get();
        } else {
            throw new RuntimeException("Invoice not found with ID: " + id);
        }
    }

}
