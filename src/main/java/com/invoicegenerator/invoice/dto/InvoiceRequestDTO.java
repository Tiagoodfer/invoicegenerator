package com.invoicegenerator.invoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class InvoiceRequestDTO {

    private UUID emitterCompanyId; // ID da empresa emissora
    private UUID recipientCompanyId; // ID da empresa destinatária
    private String invoiceNumber;
    private String comments;
    private int tax;
    private int taxRate;
    private int anotherValue;
    private List<InvoiceItemDTO> items; // Lista de itens de invoice

}
