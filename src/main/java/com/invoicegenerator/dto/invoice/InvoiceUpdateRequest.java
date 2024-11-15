package com.invoicegenerator.dto.invoice;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.List;


@Getter
@Setter
public class InvoiceUpdateRequest {

    @NotNull
    private UUID uuid;

    @NotNull
    private UUID companyId;

    @NotNull
    private UUID companyClientId;

    @NotNull
    private String invoiceNumber;

    @NotNull
    private List<InvoiceItemRequest> items;

    @NotNull
    private String comments;

    @NotNull
    private int tax;

    @NotNull
    private int anotherValue;

}
