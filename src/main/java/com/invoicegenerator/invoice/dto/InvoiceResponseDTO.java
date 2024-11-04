package com.invoicegenerator.invoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class InvoiceResponseDTO {

    private UUID uuid;
    private String invoiceNumber;
    private LocalDateTime dataCriacao;
    private String emitterCompanyName;
    private String recipientCompanyName;
    private String comments;
    private int tax;
    private int taxRate;
    private int anotherValue;
    private double subtotal;
    private double taxAmount;
    private double total;
    private List<InvoiceItemDTO> items;

}
