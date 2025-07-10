package com.example.demo;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> findAll();
    Optional<User> findById(String id);
    User save(User greeting);
    Optional<User> update(String id, User greeting);
    boolean delete(String id);
    boolean exists(String id);
}