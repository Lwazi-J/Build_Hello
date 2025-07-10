package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GreetingDaoTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDaoImpl();
    }

    @Test
    @DisplayName("Test save greeting")
    void testSave() {
        // Given
        User greeting = new User(null, "Test message", "Test Name");

        // When
        User saved = userDAO.save(greeting);

        // Then
        assertNotNull(saved);
        assertEquals("1", saved.getId());
        assertEquals(greeting.getMessage(), saved.getMessage());
        assertEquals(greeting.getName(), saved.getName());
    }

    @Test
    @DisplayName("Test find all greetings")
    void testFindAll() {
        // Given
        userDAO.save(new User(null, "First message", "First Name"));
        userDAO.save(new User(null, "Second message", "Second Name"));

        // When
        List<User> results = userDAO.findAll();

        // Then
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(g -> g.getMessage().equals("First message")));
        assertTrue(results.stream().anyMatch(g -> g.getMessage().equals("Second message")));
        assertTrue(results.stream().anyMatch(g -> g.getName().equals("First Name")));
        assertTrue(results.stream().anyMatch(g -> g.getName().equals("Second Name")));
    }

    @Test
    @DisplayName("Test find greeting by ID")
    void testFindById() {
        // Given
        User saved = userDAO.save(new User(null, "Test message", "Test Name"));

        // When
        Optional<User> found = userDAO.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(saved.getMessage(), found.get().getMessage());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    @DisplayName("Test find non-existent greeting")
    void testFindById_NotFound() {
        // When
        Optional<User> result = userDAO.findById("999");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test update greeting")
    void testUpdate() {
        // Given
        User original = userDAO.save(new User(null, "Original message", "Original Name"));
        User updated = new User(original.getId(), "Updated message", "Updated Name");

        // When
        Optional<User> result = userDAO.update(original.getId(), updated);

        // Then
        assertTrue(result.isPresent());
        assertEquals(original.getId(), result.get().getId());
        assertEquals("Updated message", result.get().getMessage());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    @DisplayName("Test update non-existent greeting")
    void testUpdate_NotFound() {
        // Given
        User updated = new User("999", "Updated message", "Updated Name");

        // When
        Optional<User> result = userDAO.update("999", updated);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test delete greeting")
    void testDelete() {
        // Given
        User saved = userDAO.save(new User(null, "Test message", "Test Name"));

        // When
        boolean deleted = userDAO.delete(saved.getId());

        // Then
        assertTrue(deleted);
        assertTrue(userDAO.findAll().isEmpty());
    }

    @Test
    @DisplayName("Test delete non-existent greeting")
    void testDelete_NotFound() {
        // When
        boolean result = userDAO.delete("999");

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test exists")
    void testExists() {
        // Given
        User saved = userDAO.save(new User(null, "Test message", "Test Name"));

        // When & Then
        assertTrue(userDAO.exists(saved.getId()));
        assertFalse(userDAO.exists("999"));
    }

    @Test
    @DisplayName("Test sequential ID generation")
    void testSequentialIdGeneration() {
        // When
        User first = userDAO.save(new User(null, "First", "First Name"));
        User second = userDAO.save(new User(null, "Second", "Second Name"));
        User third = userDAO.save(new User(null, "Third", "Third Name"));

        // Then
        assertEquals("1", first.getId());
        assertEquals("2", second.getId());
        assertEquals("3", third.getId());
    }
}