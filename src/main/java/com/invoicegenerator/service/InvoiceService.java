package com.invoicegenerator.service;

import com.invoicegenerator.domain.*;
import com.invoicegenerator.dto.company.CompanyResponse;
import com.invoicegenerator.dto.company.address.AddressDTO;
import com.invoicegenerator.dto.invoice.*;
import com.invoicegenerator.dto.usercompany.UserCompanyDTO;
import com.invoicegenerator.repository.CompanyRepository;
import com.invoicegenerator.repository.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public InvoiceResponse create(InvoiceCreateRequest invoiceRequest) {
        Invoice invoice = toInvoice(invoiceRequest);

        Company company = companyRepository.findById(invoiceRequest.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Emitter company not found with ID: " + invoiceRequest.getCompanyId()));

        Company companyClient = companyRepository.findById(invoiceRequest.getCompanyClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client company not found with ID: " + invoiceRequest.getCompanyClientId()));

        invoice.setCompany(company);
        invoice.setCompanyClient(companyClient);

        invoice.setItems(invoice.getItems().stream()
                .peek(item -> item.setInvoice(invoice))
                .collect(Collectors.toList()));

        calculateInvoiceValues(invoice);

        return toResponse(invoiceRepository.save(invoice));
    }

    public void delete(UUID id) {
        invoiceRepository.deleteById(id);
    }

    public List<InvoiceResponse> allInvoice() {
        return invoiceRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Invoice getInvoiceById(UUID id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + id));
    }

    public void calculateInvoiceValues(Invoice invoice) {
        double subtotal = invoice.getItems().stream()
                .mapToDouble(InvoiceItem::getUnitPrice)
                .sum();

        double tax = subtotal * (invoice.getTax() / 100.0);

        double other = invoice.getAnotherValue();

        double total = subtotal - tax - other;

        invoice.setSubtotal(subtotal);
        invoice.setTaxAmount(tax);
        invoice.setTotal(total);
    }


    @Transactional
    public InvoiceResponse update(UUID invoiceId, InvoiceUpdateRequest invoiceRequest) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with ID: " + invoiceId));

        invoice.setInvoiceNumber(invoiceRequest.getInvoiceNumber());
        invoice.setComments(invoiceRequest.getComments());
        invoice.setTax(invoiceRequest.getTax());
        invoice.setAnotherValue(invoiceRequest.getAnotherValue());

        Company company = companyRepository.findById(invoiceRequest.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Emitter company not found with ID: " + invoiceRequest.getCompanyId()));
        Company companyClient = companyRepository.findById(invoiceRequest.getCompanyClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client company not found with ID: " + invoiceRequest.getCompanyClientId()));

        invoice.setCompany(company);
        invoice.setCompanyClient(companyClient);

        Map<UUID, InvoiceItem> existingItemsMap = invoice.getItems().stream()
                .collect(Collectors.toMap(InvoiceItem::getUuid, item -> item));

        List<InvoiceItem> updatedItems = new ArrayList<>();

        for (InvoiceItemRequest itemRequest : invoiceRequest.getItems()) {
            if (itemRequest.getUuid() != null && existingItemsMap.containsKey(itemRequest.getUuid())) {
                InvoiceItem existingItem = existingItemsMap.get(itemRequest.getUuid());
                existingItem.setDescription(itemRequest.getDescription());
                existingItem.setUnitPrice(itemRequest.getUnitPrice());
                updatedItems.add(existingItem);
            } else {
                InvoiceItem newItem = toInvoiceItem(itemRequest);
                newItem.setInvoice(invoice);
                updatedItems.add(newItem);
            }
        }

        calculateInvoiceValues(invoice);

        return toResponse(invoiceRepository.save(invoice));
    }

    private Invoice toInvoice(InvoiceCreateRequest request) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setComments(request.getComments());
        invoice.setTax(request.getTax());
        invoice.setAnotherValue(request.getAnotherValue());
        invoice.setItems(request.getItems().stream()
                .map(this::toInvoiceItem)
                .collect(Collectors.toList()));
        return invoice;
    }

    private InvoiceItem toInvoiceItem(InvoiceItemRequest request) {
        InvoiceItem item = new InvoiceItem();
        item.setDescription(request.getDescription());
        item.setUnitPrice(request.getUnitPrice());
        return item;
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setUuid(invoice.getUuid());
        response.setCompany(toCompanyResponse(invoice.getCompany()));
        response.setCompanyClient(toCompanyResponse(invoice.getCompanyClient()));
        response.setDataCriacao(invoice.getDataCriacao());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setItems(invoice.getItems().stream()
                .map(this::toResponseItem)
                .collect(Collectors.toList()));
        response.setComments(invoice.getComments());
        response.setTax(invoice.getTax());
        response.setAnotherValue(invoice.getAnotherValue());
        response.setSubtotal(invoice.getSubtotal());
        response.setTaxAmount(invoice.getTaxAmount());
        response.setTotal(invoice.getTotal());
        return response;
    }

    private InvoiceItemResponse toResponseItem(InvoiceItem item) {
        InvoiceItemResponse response = new InvoiceItemResponse();
        response.setUuid(item.getUuid());
        response.setDescription(item.getDescription());
        response.setUnitPrice(item.getUnitPrice());
        return response;
    }

    private CompanyResponse toCompanyResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        response.setUuid(company.getUuid());
        response.setName(company.getName());
        response.setPhoneNumber(company.getPhoneNumber());
        response.setEmail(company.getEmail());

        if (company.getAddress() != null) {
            response.setAddressDTO(toAddressDTO(company.getAddress()));
        }

        if (company.getUserCompany() != null) {
            response.setUserCompanyDTO(toUserCompanyDTO(company.getUserCompany()));
        }

        return response;
    }

    private AddressDTO toAddressDTO(Address address) {
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
