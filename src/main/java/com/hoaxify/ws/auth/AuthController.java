package com.hoaxify.ws.auth;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.Views;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/1.0/auth")
    @JsonView(Views.Base.class)
    ResponseEntity<?> handleAuthentication(@RequestHeader(name = "Authorization") String authorization) {
        String base64encoded = authorization.split("Basic ")[1];// dXNlcjE6UDRzc3dvcmQ=
        String decoded = new String(Base64.getDecoder().decode(base64encoded)); // user1:P4ssword
        String[] parts = decoded.split(":"); // ["user1", "P4ssword"]
        String username = parts[0];
        User inDB = userRepository.findByUsername(username);
        return ResponseEntity.ok(inDB);
    }

}
