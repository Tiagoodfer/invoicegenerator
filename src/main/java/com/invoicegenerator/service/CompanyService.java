package com.invoicegenerator.service;

import com.invoicegenerator.domain.Address;
import com.invoicegenerator.domain.Company;
import com.invoicegenerator.domain.User;
import com.invoicegenerator.domain.UserCompany;
import com.invoicegenerator.dto.company.CompanyCreateRequest;
import com.invoicegenerator.dto.company.CompanyResponse;
import com.invoicegenerator.dto.company.CompanyUpdateRequest;
import com.invoicegenerator.dto.company.address.AddressDTO;
import com.invoicegenerator.dto.usercompany.UserCompanyDTO;
import com.invoicegenerator.repository.CompanyRepository;
import com.invoicegenerator.repository.UserCompanyRepository;
import com.invoicegenerator.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserCompanyRepository userCompanyRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CompanyResponse create(CompanyCreateRequest companyRequest) {
        UserCompany userCompany = new UserCompany();
        UUID userId = companyRequest.getUserCompanyDTO().getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userCompany.setUser(user);
        userCompany.setOwner(companyRequest.getUserCompanyDTO().isOwner());
        UserCompany savedUserCompany = userCompanyRepository.save(userCompany);

        Company company = toCompany(companyRequest);
        company.setUserCompany(savedUserCompany);

        Company savedCompany = companyRepository.save(company);

        savedUserCompany.setCompany(savedCompany);
        userCompanyRepository.save(savedUserCompany);

        return toResponse(savedCompany);
    }

    public List<CompanyResponse> findAllCompany() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        companyRepository.delete(company);
    }

    @Transactional
    public CompanyResponse update(UUID companyId, CompanyUpdateRequest companyRequest) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        company.setName(companyRequest.getName());
        company.setPhoneNumber(companyRequest.getPhoneNumber());
        company.setEmail(companyRequest.getEmail());

        Address address = company.getAddress();
        AddressDTO addressDTO = companyRequest.getAddressDTO();
        if (address == null) {
            address = new Address();
            company.setAddress(address);
        }
        address.setAddressOne(addressDTO.getAddressOne());
        address.setAddressTwo(addressDTO.getAddressTwo());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostCode(addressDTO.getPostCode());
        address.setCountry(addressDTO.getCountry());

        Company updatedCompany = companyRepository.save(company);

        return toResponse(updatedCompany);
    }

    private Company toCompany(CompanyCreateRequest companyRequest) {
        Company company = new Company();
        company.setName(companyRequest.getName());
        company.setPhoneNumber(companyRequest.getPhoneNumber());
        company.setEmail(companyRequest.getEmail());
        company.setAddress(toAddress(companyRequest.getAddressDTO()));
        return company;
    }

    private Address toAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setAddressOne(addressDTO.getAddressOne());
        address.setAddressTwo(addressDTO.getAddressTwo());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostCode(addressDTO.getPostCode());
        address.setCountry(addressDTO.getCountry());
        return address;
    }

    public CompanyResponse toResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        response.setUuid(company.getUuid());
        response.setName(company.getName());
        response.setPhoneNumber(company.getPhoneNumber());
        response.setEmail(company.getEmail());
        response.setAddressDTO(toDTO(company.getAddress()));

        if (company.getUserCompany() != null) {
            response.setUserCompanyDTO(toUserCompanyDTO(company.getUserCompany()));
        }

        return response;
    }

    private AddressDTO toDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressOne(address.getAddressOne());
        addressDTO.setAddressTwo(address.getAddressTwo());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setPostCode(address.getPostCode());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    private UserCompanyDTO toUserCompanyDTO(UserCompany userCompany) {
        UserCompanyDTO userCompanyDTO = new UserCompanyDTO();
        userCompanyDTO.setUserId(userCompany.getUser().getId());
        userCompanyDTO.setCompanyId(userCompany.getCompany().getUuid());
        userCompanyDTO.setOwner(userCompany.isOwner());
        return userCompanyDTO;
    }

}
