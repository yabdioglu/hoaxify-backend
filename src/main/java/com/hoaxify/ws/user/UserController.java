package com.hoaxify.ws.user;

import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.vm.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/1.0/users")
    public GenericResponse createUser(@Valid @RequestBody User user){
        userService.save(user);
        return new GenericResponse("user created");
    }

    @GetMapping("/api/1.0/users")
    Page<UserVM> getUsers(Pageable pageable){
        return userService.getUsers(pageable).map(UserVM::new);
    }
}
