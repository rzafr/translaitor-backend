package com.translaitor.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDto {

    @NotNull
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @Email
    private String email;
    private String phoneNumber;

}
