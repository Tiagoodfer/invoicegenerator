package com.invoicegenerator.recipientCompany.repository;

import com.invoicegenerator.recipientCompany.RecipientCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipientCompanyRepository extends JpaRepository<RecipientCompany, UUID> {
}
