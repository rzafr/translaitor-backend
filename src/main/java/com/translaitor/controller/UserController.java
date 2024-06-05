package com.translaitor.controller;

import com.translaitor.service.UserService;
import com.translaitor.service.dto.GetUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')") // Check SecurityConfig.configure()
    @GetMapping("/users")
    public ResponseEntity<List<GetUserDto>> getAllUsers() { return buildResponseOfAList(userService.findAll()); }

    @GetMapping("/users/{username}")
    public ResponseEntity<GetUserDto> getUserByUsername(@PathVariable("username") String username) {
        Optional<GetUserDto> getUserDto = userService.findByUsernameDto(username);
        return !getUserDto.isPresent() ? ResponseEntity.notFound().build() : ResponseEntity.of(getUserDto);
    }

    @PutMapping("/users")
    public ResponseEntity<GetUserDto> updateUser(@RequestBody GetUserDto userDto) {
        Optional<GetUserDto> getUserDto = userService.updateUser(userDto);
        return !getUserDto.isPresent() ? ResponseEntity.notFound().build() : ResponseEntity.of(getUserDto);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Build the response from a List<User>
     * @param list
     * @return
     */
    private ResponseEntity<List<GetUserDto>> buildResponseOfAList(List<GetUserDto> list) {
        return list.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(list);
    }

}
