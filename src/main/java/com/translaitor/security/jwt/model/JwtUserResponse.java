package com.translaitor.security.jwt.model;

import com.translaitor.service.dto.user.GetUserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JwtUserResponse extends GetUserDto {

    private String token;

    @Builder(builderMethodName="jwtUserResponseBuilder")
    public JwtUserResponse(Long id, String username, String firstName, String lastName, LocalDate dateOfBirth, String email, String phoneNumber, List<String> roles, String token) {
        super(id, username, firstName, lastName, dateOfBirth, email, phoneNumber, roles);
        this.token = token;
    }

}