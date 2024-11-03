package com.invoicegenerator.recipientCompany.service;

import com.invoicegenerator.recipientCompany.RecipientCompany;
import com.invoicegenerator.recipientCompany.repository.RecipientCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecipientCompanyService {

    @Autowired
    private RecipientCompanyRepository recipientCompanyRepository;

    public RecipientCompany created(RecipientCompany recipientCompany) {
        return recipientCompanyRepository.save(recipientCompany);
    }

    public void delete(UUID id) {
        recipientCompanyRepository.deleteById(id);
    }

    public List<RecipientCompany> findAllCompanies() {
        return recipientCompanyRepository.findAll();
    }

    public RecipientCompany update(UUID uuid, RecipientCompany updateRecipientCompany) {
        RecipientCompany upRecipientCompany = recipientCompanyRepository.findById(uuid).get();

        upRecipientCompany.setName(updateRecipientCompany.getName());
        upRecipientCompany.setAddress(updateRecipientCompany.getAddress());
        upRecipientCompany.setPhoneNumber(updateRecipientCompany.getPhoneNumber());
        upRecipientCompany.setEmail(updateRecipientCompany.getEmail());
        return recipientCompanyRepository.save(upRecipientCompany);
    }

}
