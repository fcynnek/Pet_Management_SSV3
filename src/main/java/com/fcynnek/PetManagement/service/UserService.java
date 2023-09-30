package com.fcynnek.PetManagement.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.fcynnek.PetManagement.domain.User;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    List<User> findAll();
}