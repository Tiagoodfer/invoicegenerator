package com.invoicegenerator.repository;

import com.invoicegenerator.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

}
