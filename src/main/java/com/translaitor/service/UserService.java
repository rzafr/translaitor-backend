package com.translaitor.service;

import com.translaitor.error.exceptions.NewUserWithDifferentPasswordsException;
import com.translaitor.model.User;
import com.translaitor.model.UserRole;
import com.translaitor.repository.UserRepository;
import com.translaitor.service.dto.CreateUserDto;
import com.translaitor.service.dto.GetUserDto;
import com.translaitor.service.dto.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<GetUserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userDtoConverter::convertUserToGetUserDto)
                .collect(Collectors.toList());
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
