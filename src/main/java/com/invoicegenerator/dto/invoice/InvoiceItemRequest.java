package com.invoicegenerator.dto.invoice;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class InvoiceItemRequest {

    private UUID uuid;

    @NotNull
    private String description;

    @NotNull
    private double unitPrice;

}
