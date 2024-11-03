package com.invoicegenerator.emitterCompany.service;

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
        EmitterCompany upEmitterCompany = emitterCompanyRepository.findById(uuid).get();

        upEmitterCompany.setName(updateEmitterCompany.getName());
        upEmitterCompany.setAddress(updateEmitterCompany.getAddress());
        upEmitterCompany.setPhoneNumber(updateEmitterCompany.getPhoneNumber());
        upEmitterCompany.setEmail(updateEmitterCompany.getEmail());
        return emitterCompanyRepository.save(upEmitterCompany);
    }

}
