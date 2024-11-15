package com.invoicegenerator.repository;

import com.invoicegenerator.domain.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCompanyRepository extends JpaRepository<UserCompany, UUID> {
}
