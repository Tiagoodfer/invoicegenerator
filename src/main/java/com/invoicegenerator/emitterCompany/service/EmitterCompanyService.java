package com.invoicegenerator.emitterCompany.service;

import com.invoicegenerator.address.Address;
import com.invoicegenerator.emitterCompany.EmitterCompany;
import com.invoicegenerator.emitterCompany.repository.EmitterCompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.sql.results.graph.embeddable.internal.EmbeddableFetchImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmitterCompanyService {

    @Autowired
    private EmitterCompanyRepository emitterCompanyRepository;

    public EmitterCompany create(EmitterCompany emitterCompany) {
        return emitterCompanyRepository.save(emitterCompany);
    }

    public void delete(UUID id) {
        emitterCompanyRepository.deleteById(id);
    }

    public List<EmitterCompany> findAllCompanies() {
        return emitterCompanyRepository.findAll();
    }

    public EmitterCompany update(UUID uuid, EmitterCompany updateEmitterCompany) {
        EmitterCompany existingEmitterCompany = emitterCompanyRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("EmitterCompany not found"));

        existingEmitterCompany.setName(updateEmitterCompany.getName());
        existingEmitterCompany.setPhoneNumber(updateEmitterCompany.getPhoneNumber());
        existingEmitterCompany.setEmail(updateEmitterCompany.getEmail());

        Address updatedAddress = updateEmitterCompany.getAddress();
        if (updatedAddress != null) {
            Address currentAddress = existingEmitterCompany.getAddress();

            if (currentAddress != null) {
                currentAddress.setAddressOne(updatedAddress.getAddressOne());
                currentAddress.setAddressTwo(updatedAddress.getAddressTwo());
                currentAddress.setCity(updatedAddress.getCity());
                currentAddress.setState(updatedAddress.getState());
                currentAddress.setPostCode(updatedAddress.getPostCode());
            } else {
                existingEmitterCompany.setAddress(updatedAddress);
            }
        }

        return emitterCompanyRepository.save(existingEmitterCompany);
    }

}
