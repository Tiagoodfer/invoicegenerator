package com.invoicegenerator.service;

import com.invoicegenerator.domain.User;
import com.invoicegenerator.domain.UserCompany;
import com.invoicegenerator.dto.user.UserDTO;
import com.invoicegenerator.dto.usercompany.UserCompanyDTO;
import com.invoicegenerator.repository.UserCompanyRepository;
import com.invoicegenerator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCompanyRepository userCompanyRepository;

    public UserDTO create(UserDTO userDTO) {
        User user = toUser(userDTO);
        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public UserDTO update(UUID uuid, UserDTO userDTO) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

       user.setName(userDTO.getName());
       user.setLogin(userDTO.getLogin());
       user.setPassword(userDTO.getPassword());

        User updatedUser = userRepository.save(user);

        return toDTO(updatedUser);
    }



    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());

        if (user.getUserCompanies() != null) {
            userDTO.setUserCompanies(user.getUserCompanies()
                    .stream()
                    .map(this::toUserCompanyDTO)
                    .collect(Collectors.toList()));
        }
        return userDTO;
    }

    public UserCompanyDTO toUserCompanyDTO(UserCompany userCompany) {
        UserCompanyDTO userCompanyDTO = new UserCompanyDTO();
        userCompanyDTO.setUserId(userCompany.getUser().getId());
        userCompanyDTO.setCompanyId(userCompany.getCompany().getUuid());
        userCompanyDTO.setOwner(userCompany.isOwner());
        return userCompanyDTO;
    }

    public User toUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        return user;
    }

}
