package com.invoicegenerator.dto.company.address;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddressDTO {

    @NotNull
    private String addressOne;

    private String addressTwo;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    private String postCode;

    @NotNull
    private String country;
}
