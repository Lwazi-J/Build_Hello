package com.example.demo;

import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

public class UserDaoImpl implements UserDAO {

    private final List<User> greetings = Collections.synchronizedList(new ArrayList<>());
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public List<User> findAll() {
        log.debug("Finding all greetings");
        synchronized(greetings) {
            return new ArrayList<>(greetings);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        log.debug("Finding greeting with id: {}", id);
        synchronized(greetings) {
            return greetings.stream().filter(g -> g.getId().equals(id)).findFirst();
        }
    }

    @Override
    public User save(User greeting) {
        log.debug("Saving new greeting: {}", greeting);
        synchronized(greetings) {
            String id = String.valueOf(greetings.size() + 1);
            User newGreeting = new User(id, greeting.getMessage(), greeting.getName()); // Include the name field
            greetings.add(newGreeting);
            return newGreeting;
        }
    }

    @Override
    public Optional<User> update(String id, User greeting) {
        log.debug("Updating greeting with id: {}", id);
        synchronized(greetings) {
            Optional<User> existingGreeting = findById(id);
            if (existingGreeting.isEmpty()) {
                return Optional.empty();
            }

            int index = greetings.indexOf(existingGreeting.get());
            User updatedGreeting = new User(id, greeting.getMessage(), greeting.getName());  // Include the name field

            greetings.set(index, updatedGreeting);
            return Optional.of(updatedGreeting);
        }
    }

    @Override
    public boolean delete(String id) {
        log.debug("Deleting greeting with id: {}", id);
        synchronized(greetings) {
            return findById(id).map(greetings::remove).orElse(false);
        }
    }

    @Override
    public boolean exists(String id) {
        log.debug("Checking if greeting exists with id: {}", id);
        synchronized(greetings) {
            return greetings.stream().anyMatch(g -> g.getId().equals(id));
        }
    }
}