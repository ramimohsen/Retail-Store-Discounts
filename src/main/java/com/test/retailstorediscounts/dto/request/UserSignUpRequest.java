package com.test.retailstorediscounts.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequest {

    private Set<String> roles;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotNull
    @Size(min = 6, max = 30)
    private String password;
}
