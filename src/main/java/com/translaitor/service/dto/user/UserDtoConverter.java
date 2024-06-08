package com.translaitor.service.dto.user;

import com.translaitor.model.User;
import com.translaitor.model.UserRole;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDtoConverter {

    public GetUserDto convertUserToGetUserDto(User user) {
        return GetUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles().stream()
                        .map(UserRole::name)
                        .collect(Collectors.toList())
                ).build();
    }

}
