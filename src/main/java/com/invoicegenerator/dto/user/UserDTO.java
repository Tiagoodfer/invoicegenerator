package com.invoicegenerator.dto.user;

import com.invoicegenerator.dto.usercompany.UserCompanyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;

    @NotNull
    private String name;

    private List<UserCompanyDTO> userCompanies;

    @NotNull
    private String login;

    @NotNull
    private String password;
}
