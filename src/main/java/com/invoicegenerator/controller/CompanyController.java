package com.invoicegenerator.controller;

import com.invoicegenerator.dto.company.CompanyCreateRequest;
import com.invoicegenerator.dto.company.CompanyResponse;
import com.invoicegenerator.dto.company.CompanyUpdateRequest;
import com.invoicegenerator.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@RequestBody CompanyCreateRequest companyRequest) {
        CompanyResponse savedCompany = companyService.create(companyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> companies = companyService.findAllCompany();
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> update(@PathVariable("id") UUID id, @RequestBody CompanyUpdateRequest companyRequest) {
        CompanyResponse updatedCompany = companyService.update(id, companyRequest);
        return ResponseEntity.ok(updatedCompany);
    }

}
