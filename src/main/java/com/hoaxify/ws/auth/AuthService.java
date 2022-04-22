package com.hoaxify.ws.auth;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import com.hoaxify.ws.user.UserService;
import com.hoaxify.ws.user.vm.UserVM;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(Credentials credentials) {
        User inDB = userRepository.findByUsername(credentials.getUsername());
        if (inDB == null) {
            throw new AuthException();
        }
        boolean matches = passwordEncoder.matches(credentials.getPassword(), inDB.getPassword());
        if (!matches) {
            throw new AuthException();
        }
        UserVM userVM = new UserVM(inDB);
        String token = Jwts.builder().setSubject("" + inDB.getId()).signWith(SignatureAlgorithm.HS512, "my-app-scret").compact(); // key gizli olmalı
        // setSubject istediğimiz bir şey olabilir biz burda kullanıcının id'sini verdik
        AuthResponse response = new AuthResponse();
        response.setUser(userVM);
        response.setToken(token);
        return response;
    }
}
