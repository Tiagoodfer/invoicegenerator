package com.invoicegenerator.emitterCompany.repository;

import com.invoicegenerator.emitterCompany.EmitterCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmitterCompanyRepository extends JpaRepository<EmitterCompany, UUID> {
}
