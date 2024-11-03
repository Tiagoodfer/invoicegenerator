package com.invoicegenerator.recipientCompany.controller;

import com.invoicegenerator.recipientCompany.RecipientCompany;
import com.invoicegenerator.recipientCompany.service.RecipientCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipient")
public class RecipientCompanyController {

    @Autowired
    private RecipientCompanyService recipientCompanyService;

    @PostMapping
    public ResponseEntity<RecipientCompany> created(@RequestBody RecipientCompany recipientCompany) {
        RecipientCompany recipientCompany1 = recipientCompanyService.created(recipientCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipientCompany1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        recipientCompanyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RecipientCompany>> getAllCompanies() {
        List<RecipientCompany> recipientCompanies = recipientCompanyService.findAllCompanies();
        return ResponseEntity.ok(recipientCompanies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipientCompany> update(
            @PathVariable("id") UUID id,
            @RequestBody RecipientCompany updateRecipientCompany) {
        RecipientCompany upRecipientCompany = recipientCompanyService.update(id, updateRecipientCompany);
        return ResponseEntity.ok(updateRecipientCompany);
    }

}
