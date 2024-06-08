package com.translaitor.service.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDto {

    @NotEmpty(message = "El nombre de usuario es requerido.")
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String verifyPassword;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @Email
    private String email;
    private String phoneNumber;

}
