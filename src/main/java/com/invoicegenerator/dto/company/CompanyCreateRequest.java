package com.invoicegenerator.dto.company;

import com.invoicegenerator.dto.company.address.AddressDTO;
import com.invoicegenerator.dto.usercompany.UserCompanyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequest {

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String email;

    @NotNull
    private AddressDTO address;

    @NotNull
    private UserCompanyDTO userCompany;

}