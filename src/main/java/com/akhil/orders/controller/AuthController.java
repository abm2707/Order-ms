package com.akhil.orders.controller;

import com.akhil.orders.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/auth/token/user")
    public String token() {
        return jwtUtil.generateToken(
                "user-akhil",
                List.of("ROLE_USER")
        );
    }

    @PostMapping("/auth/token/admin")
    public String adminToken() {
        return jwtUtil.generateToken(
                "admin-akhil",
                List.of("ROLE_ADMIN")
        );
    }
}
