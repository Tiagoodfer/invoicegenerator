package com.invoicegenerator.dto.company;

import com.invoicegenerator.dto.company.address.AddressDTO;
import com.invoicegenerator.dto.usercompany.UserCompanyDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyResponse {

    private UUID uuid;

    private String name;

    private AddressDTO addressDTO;

    private String phoneNumber;

    private String email;

    private UserCompanyDTO userCompanyDTO;
}
