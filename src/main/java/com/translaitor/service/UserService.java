package com.translaitor.service;

import com.translaitor.exception.NewUserWithDifferentPasswordsException;
import com.translaitor.model.User;
import com.translaitor.model.UserRole;
import com.translaitor.repository.UserRepository;
import com.translaitor.service.dto.user.CreateUserDto;
import com.translaitor.service.dto.user.GetUserDto;
import com.translaitor.service.dto.user.UpdateUserDto;
import com.translaitor.service.dto.user.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
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

    public User createUser(CreateUserDto newUser) {
        if (newUser.getPassword().contentEquals(newUser.getVerifyPassword())) {
            if (userRepository.existsByUsername(newUser.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username already exists");
            }
            User user = User.builder()
                    .username(newUser.getUsername())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .firstName(newUser.getFirstName())
                    .lastName(newUser.getLastName())
                    .dateOfBirth(newUser.getDateOfBirth())
                    .email(newUser.getEmail())
                    .phoneNumber(newUser.getPhoneNumber())
                    .roles(List.of(UserRole.USER))
                    .build();
            try {
                return userRepository.save(user);
            } catch (DataIntegrityViolationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving user", ex);
            }
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }
    }

    public List<GetUserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userDtoConverter::convertUserToGetUserDto)
                .collect(Collectors.toList());
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<GetUserDto> findByUsernameDto(String username) {
        return userRepository.findByUsername(username).stream()
                .map(userDtoConverter::convertUserToGetUserDto)
                .findFirst();
    }

    public Optional<GetUserDto> updateUser(UpdateUserDto updatedUser) {
        User user = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + updatedUser.getId()));;

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        return Optional.ofNullable(userDtoConverter.convertUserToGetUserDto(userRepository.save(user)));
    }

    public void delete(User user) {
        deleteById(user.getId());
    }

    public void deleteById(Long id) {
        userRepository.findById(id).ifPresent(user -> userRepository.delete(user));
    }



}
