package com.translaitor.controller;

import com.translaitor.service.UserService;
import com.translaitor.service.dto.CreateUserDto;
import com.translaitor.service.dto.GetUserDto;
import com.translaitor.service.dto.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    @PostMapping("/user")
    public ResponseEntity<GetUserDto> createUser(@RequestBody CreateUserDto newUser) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userDtoConverter.convertUserToGetUserDto(userService.createUser(newUser)));

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
