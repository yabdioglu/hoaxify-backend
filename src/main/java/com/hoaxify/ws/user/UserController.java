package com.hoaxify.ws.user;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericResponse;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/1.0/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user){
        ApiError error = new ApiError(400,"Validation error", "/api/1.0/users");
        Map<String, String> validationErrors = new HashMap<>();

        String username = user.getUsername();
        String displayName = user.getDisplayName();

        if(username == null || username.isEmpty()){
            validationErrors.put("username", "username cannot be null");
        }

        if(displayName == null || displayName.isEmpty()){
            validationErrors.put("displayName", "cannot be null");
        }

        if(validationErrors.size() > 0){
            error.setValidationErrors(validationErrors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        userService.save(user);
        return ResponseEntity.ok(new GenericResponse("user created"));
    }
}
