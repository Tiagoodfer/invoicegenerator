package com.invoicegenerator.emitterCompany.controller;

import com.invoicegenerator.emitterCompany.EmitterCompany;
import com.invoicegenerator.emitterCompany.service.EmitterCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/emitter")
public class EmitterCompanyController {

    @Autowired
    private EmitterCompanyService emitterCompanyService;

    @PostMapping
    public ResponseEntity<EmitterCompany> create(@RequestBody EmitterCompany emitterCompany) {
        EmitterCompany savedCompany = emitterCompanyService.create(emitterCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        emitterCompanyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmitterCompany>> getAllCompanies() {
        List<EmitterCompany> companies = emitterCompanyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmitterCompany> update(
            @PathVariable("id") UUID id,
            @RequestBody EmitterCompany updatedEmitterCompany) {
        EmitterCompany emitterCompany = emitterCompanyService.update(id, updatedEmitterCompany);
        return ResponseEntity.ok(emitterCompany);
    }

}
