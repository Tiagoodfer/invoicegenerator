package com.invoicegenerator.recipientCompany.service;

import com.invoicegenerator.address.Address;
import com.invoicegenerator.recipientCompany.RecipientCompany;
import com.invoicegenerator.recipientCompany.repository.RecipientCompanyRepository;
import jakarta.persistence.EntityNotFoundException;
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
        RecipientCompany existingRecipientCompany = recipientCompanyRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("RecipientCompany not found"));

        existingRecipientCompany.setName(updateRecipientCompany.getName());
        existingRecipientCompany.setPhoneNumber(updateRecipientCompany.getPhoneNumber());
        existingRecipientCompany.setEmail(updateRecipientCompany.getEmail());

        Address updatedAddress = updateRecipientCompany.getAddress();
        if (updatedAddress != null) {
            Address currentAddress = existingRecipientCompany.getAddress();

            if (currentAddress != null) {
                currentAddress.setAddressOne(updatedAddress.getAddressOne());
                currentAddress.setAddressTwo(updatedAddress.getAddressTwo());
                currentAddress.setCity(updatedAddress.getCity());
                currentAddress.setState(updatedAddress.getState());
                currentAddress.setPostCode(updatedAddress.getPostCode());
            } else {
                existingRecipientCompany.setAddress(updatedAddress);
            }
        }

        return recipientCompanyRepository.save(existingRecipientCompany);
    }


}
