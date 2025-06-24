package com.example.demo;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllGreetings();
    Optional<User> getGreetingById(String id);
    User createGreeting(UserRequest request);
    Optional<User> updateGreeting(String id, UserRequest request);
    boolean deleteGreeting(String id);
}