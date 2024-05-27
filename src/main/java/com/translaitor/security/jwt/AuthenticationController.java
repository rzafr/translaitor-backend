package com.translaitor.security.jwt;

import com.translaitor.model.User;
import com.translaitor.model.UserRole;
import com.translaitor.security.jwt.model.JwtUserResponse;
import com.translaitor.security.jwt.model.LoginRequest;
import com.translaitor.service.UserService;
import com.translaitor.service.dto.CreateUserDto;
import com.translaitor.service.dto.GetUserDto;
import com.translaitor.service.dto.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDtoConverter userDtoConverter;
    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<JwtUserResponse> createUser(@RequestBody CreateUserDto newUser) {
        try {
            userDtoConverter.convertUserToGetUserDto(userService.createUser(newUser));
            LoginRequest loginRequest = new LoginRequest(newUser.getUsername(), newUser.getPassword());
            return login(loginRequest);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtUserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtTokenProvider.generateToken(authentication);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertUserAndTokenToJwtUserResponse(user, jwtToken));

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/me")
    public GetUserDto me(@AuthenticationPrincipal User user) {
        return userDtoConverter.convertUserToGetUserDto(user);
    }

    /**
     * Transforms a user entity and the token into a JwtUserResponse object
     * @param user
     * @param jwtToken
     * @return
     */
    private JwtUserResponse convertUserAndTokenToJwtUserResponse(User user, String jwtToken) {
        return JwtUserResponse
                .jwtUserResponseBuilder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles().stream().map(UserRole::name).collect(Collectors.toSet()))
                .token(jwtToken)
                .build();

    }

}
