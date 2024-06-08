package com.translaitor.security.jwt.model;

import javax.validation.constraints.NotEmpty;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}