package com.translaitor.controller;

import com.translaitor.service.UserService;
import com.translaitor.service.dto.GetUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<GetUserDto> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{username}")
    public Optional<GetUserDto> getUserByUsername(@PathVariable("username") String username) {
        return userService.findByUsernameDto(username);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

}
