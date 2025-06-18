package com.usermanagement.service;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.UpdateUserRequest;
import com.usermanagement.dto.UserDto;
import com.usermanagement.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    
    UserDto createUser(CreateUserRequest request);
    
    UserDto getUserById(UUID id);
    
    UserDto getUserByUsername(String username);
    
    List<UserDto> getAllUsers();
    
    List<UserDto> getActiveUsers();
    
    UserDto updateUser(UUID id, UpdateUserRequest request);
    
    void deleteUser(UUID id);
    
    void deactivateUser(UUID id);
    
    void activateUser(UUID id);
    
    User getCurrentUser();
} 