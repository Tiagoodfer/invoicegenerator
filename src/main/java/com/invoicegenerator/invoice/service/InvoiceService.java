package com.invoicegenerator.invoice.service;

import com.invoicegenerator.emitterCompany.EmitterCompany;
import com.invoicegenerator.emitterCompany.repository.EmitterCompanyRepository;
import com.invoicegenerator.invoice.Invoice;
import com.invoicegenerator.invoice.InvoiceItem;
import com.invoicegenerator.invoice.repository.InvoiceRepository;
import com.invoicegenerator.recipientCompany.RecipientCompany;
import com.invoicegenerator.recipientCompany.repository.RecipientCompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private EmitterCompanyRepository emitterCompanyRepository;

    @Autowired
    private RecipientCompanyRepository recipientCompanyRepository;

    @Transactional
    public Invoice create(Invoice invoice) {

        UUID emitterCompanyId = invoice.getEmitterCompany().getUuid();
        UUID recipientCompanyId = invoice.getRecipientCompany().getUuid();

        EmitterCompany emitterCompany = emitterCompanyRepository.findById(emitterCompanyId)
                .orElseThrow(() -> new EntityNotFoundException("EmitterCompany not found with ID: " + emitterCompanyId));
        RecipientCompany recipientCompany = recipientCompanyRepository.findById(recipientCompanyId)
                .orElseThrow(() -> new EntityNotFoundException("RecipientCompany not found with ID: " + recipientCompanyId));

        invoice.setEmitterCompany(emitterCompany);
        invoice.setRecipientCompany(recipientCompany);

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
        Invoice existingInvoice = invoiceRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        UUID emitterCompanyId = updateInvoice.getEmitterCompany().getUuid();
        UUID recipientCompanyId = updateInvoice.getRecipientCompany().getUuid();

        EmitterCompany emitterCompany = emitterCompanyRepository.findById(emitterCompanyId)
                .orElseThrow(() -> new EntityNotFoundException("EmitterCompany not found with ID: " + emitterCompanyId));
        RecipientCompany recipientCompany = recipientCompanyRepository.findById(recipientCompanyId)
                .orElseThrow(() -> new EntityNotFoundException("RecipientCompany not found with ID: " + recipientCompanyId));

        existingInvoice.setEmitterCompany(emitterCompany);
        existingInvoice.setRecipientCompany(recipientCompany);

        existingInvoice.setInvoiceNumber(updateInvoice.getInvoiceNumber());
        existingInvoice.setComments(updateInvoice.getComments());
        existingInvoice.setTax(updateInvoice.getTax());
        existingInvoice.setTaxRate(updateInvoice.getTaxRate());
        existingInvoice.setAnotherValue(updateInvoice.getAnotherValue());

        Map<UUID, InvoiceItem> existingItemsMap = existingInvoice.getItems().stream()
                .collect(Collectors.toMap(InvoiceItem::getUuid, item -> item));

        List<InvoiceItem> updatedItems = new ArrayList<>();
        for (InvoiceItem newItem : updateInvoice.getItems()) {
            InvoiceItem existingItem = existingItemsMap.get(newItem.getUuid());

            if (existingItem != null) {
                existingItem.setDescription(newItem.getDescription());
                existingItem.setUnitPrice(newItem.getUnitPrice());
                updatedItems.add(existingItem);
            } else {
                newItem.setInvoice(existingInvoice);
                updatedItems.add(newItem);
            }
        }

        existingInvoice.setItems(updatedItems);
        sumInvoiceValues(existingInvoice);
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
