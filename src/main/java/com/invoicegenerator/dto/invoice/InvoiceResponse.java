package com.invoicegenerator.dto.invoice;

import com.invoicegenerator.dto.company.CompanyResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class InvoiceResponse {

    private UUID uuid;
    private CompanyResponse company;
    private CompanyResponse companyClient;
    private LocalDateTime dataCriacao;
    private String invoiceNumber;
    private List<InvoiceItemResponse> items;
    private String comments;
    private int tax;
    private int anotherValue;
    private double subtotal;
    private double taxAmount;
    private double total;
}
