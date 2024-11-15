package com.invoicegenerator.dto.usercompany;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class UserCompanyDTO {

    @NotNull
    private UUID userId;

    private UUID companyId;

    @NotNull
    private boolean isOwner;

}

