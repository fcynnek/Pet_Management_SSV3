package com.fcynnek.PetManagement.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fcynnek.PetManagement.domain.User;
import com.fcynnek.PetManagement.service.UserService;

import java.util.List;
@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers () {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}
