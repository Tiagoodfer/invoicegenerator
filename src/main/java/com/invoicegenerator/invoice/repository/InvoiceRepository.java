package com.invoicegenerator.invoice.repository;

import com.invoicegenerator.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

}
