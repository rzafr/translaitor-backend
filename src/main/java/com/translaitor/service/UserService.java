package com.translaitor.service;

import com.translaitor.error.exceptions.NewUserWithDifferentPasswordsException;
import com.translaitor.model.User;
import com.translaitor.model.UserRole;
import com.translaitor.repository.UserRepository;
import com.translaitor.service.dto.CreateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(CreateUserDto newUser) {
        if (newUser.getPassword().contentEquals(newUser.getVerifyPassword())) {
            User user = User.builder()
                    .username(newUser.getUsername())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .firstName(newUser.getFirstName())
                    .lastName(newUser.getLastName())
                    .dateOfBirth(newUser.getDateOfBirth())
                    .email(newUser.getEmail())
                    .phoneNumber(newUser.getPhoneNumber())
                    .roles(Set.of(UserRole.USER))
                    .build();
            try {
                return userRepository.save(user);
            } catch (DataIntegrityViolationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username already exists");
            }
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }
    }
}
