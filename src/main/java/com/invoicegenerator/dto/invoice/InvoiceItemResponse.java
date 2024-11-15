package com.invoicegenerator.dto.invoice;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InvoiceItemResponse {

    private UUID uuid;
    private String description;
    private double unitPrice;

}
