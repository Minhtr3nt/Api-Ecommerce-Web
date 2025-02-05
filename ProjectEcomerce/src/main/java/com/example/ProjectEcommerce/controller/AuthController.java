package com.example.ProjectEcommerce.controller;

import com.example.ProjectEcommerce.reponse.ApiResponse;
import com.example.ProjectEcommerce.reponse.JwtResponse;
import com.example.ProjectEcommerce.request.LoginRequest;
import com.example.ProjectEcommerce.request.ValidateTokenRequest;
import com.example.ProjectEcommerce.sercurity.jwt.JwtUtils;
import com.example.ProjectEcommerce.sercurity.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), userDetails.getEmail(), jwt);
            return ResponseEntity.ok(new ApiResponse("Login success", jwtResponse));
        } catch (AuthenticationException e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse> validate(@Valid @RequestBody ValidateTokenRequest request){
        boolean check = jwtUtils.validateToken(request.getToken());
        if(check){
            return ResponseEntity.ok(new ApiResponse("Token valid", true));
        }else{
            return ResponseEntity.ok(new ApiResponse("Token invalid, login again", false));
        }
    }
}
