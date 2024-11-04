package com.invoicegenerator.invoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemDTO {

    private String description;
    private double unitPrice;

}
