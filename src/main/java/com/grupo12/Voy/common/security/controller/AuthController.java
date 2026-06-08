package com.grupo12.Voy.common.security.controller;

import com.grupo12.Voy.common.security.dto.AuthRequest;
import com.grupo12.Voy.common.security.dto.AuthResponse;
import com.grupo12.Voy.common.security.service.AuthService;
import com.grupo12.Voy.common.security.service.JwtService;
import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Dto.UserDto;
import com.grupo12.Voy.features.users.Service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UsersService userService;
    private final JwtService jwtService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody
                                                         AuthRequest authRequest){
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody
                                                NewUserDto newAccountRequest){
        return new ResponseEntity<>(userService.newUser(newAccountRequest),
                HttpStatus.CREATED);
    }
}
