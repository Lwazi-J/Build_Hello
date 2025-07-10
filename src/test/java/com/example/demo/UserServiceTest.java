package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl greetingService;

    @BeforeEach
    void setUp() {
        greetingService = new UserServiceImpl();
    }

    private User createSampleGreeting(String name, String message) {
        return greetingService.createGreeting(new UserRequest(name, message));
    }

    @Test
    @DisplayName("Test successful greeting creation")
    void testCreateGreeting_Success() {
        // Given
        UserRequest request = new UserRequest("John", "Hello!");

        // When
        User result = greetingService.createGreeting(request);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertTrue(result.getMessage().contains("John"));
        assertTrue(result.getMessage().contains("Hello!"));
    }

    @Test
    @DisplayName("Test greeting creation with null request")
    void testCreateGreeting_NullRequest() {
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> greetingService.createGreeting(null));
        assertEquals("Request cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Test greeting creation with empty name")
    void testCreateGreeting_EmptyName() {
        // Given
        UserRequest request = new UserRequest("", "Test message");

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> greetingService.createGreeting(request));
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Test greeting creation with empty message")
    void testCreateGreeting_EmptyMessage() {
        // Given
        UserRequest request = new UserRequest("John", "");

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> greetingService.createGreeting(request));
        assertEquals("Message cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Test getting all greetings when empty")
    void testGetAllGreetings_EmptyList() {
        // When
        List<User> results = greetingService.getAllGreetings();

        // Then
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Test getting all greetings with multiple entries")
    void testGetAllGreetings_MultipleEntries() {
        // Given
        createSampleGreeting("John", "First message");
        createSampleGreeting("Jane", "Second message");

        // When
        List<User> results = greetingService.getAllGreetings();

        // Then
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(g -> g.getMessage().contains("First message")));
        assertTrue(results.stream().anyMatch(g -> g.getMessage().contains("Second message")));
    }

    @Test
    @DisplayName("Test successful greeting update")
    void testUpdateGreeting_Success() {
        // Given
        User original = createSampleGreeting("John", "Original message");
        UserRequest updateRequest = new UserRequest("John Updated", "Updated message");

        // When
        Optional<User> result = greetingService.updateGreeting(original.getId(), updateRequest);

        // Then
        assertTrue(result.isPresent());
        assertEquals(original.getId(), result.get().getId());
        assertTrue(result.get().getMessage().contains("John Updated"));
        assertTrue(result.get().getMessage().contains("Updated message"));
    }

    @Test
    @DisplayName("Test updating non-existent greeting")
    void testUpdateGreeting_NotFound() {
        // Given
        UserRequest updateRequest = new UserRequest("John", "Test");

        // When
        Optional<User> result = greetingService.updateGreeting("999", updateRequest);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test successful greeting deletion")
    void testDeleteGreeting_Success() {
        // Given
        User greeting = createSampleGreeting("John", "Test message");

        // When
        boolean result = greetingService.deleteGreeting(greeting.getId());

        // Then
        assertTrue(result);
        assertTrue(greetingService.getAllGreetings().isEmpty());
    }

    @Test
    @DisplayName("Test deleting non-existent greeting")
    void testDeleteGreeting_NotFound() {
        // When
        boolean result = greetingService.deleteGreeting("999");

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test getting existing greeting by ID")
    void testGetGreetingById_Success() {
        // Given
        User created = createSampleGreeting("John", "Test message");

        // When
        Optional<User> result = greetingService.getGreetingById(created.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(created.getId(), result.get().getId());
        assertEquals(created.getMessage(), result.get().getMessage());
    }

    @Test
    @DisplayName("Test getting non-existent greeting by ID")
    void testGetGreetingById_NotFound() {
        // When
        Optional<User> result = greetingService.getGreetingById("999");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test sequential ID generation")
    void testSequentialIdGeneration() {
        // When
        User first = createSampleGreeting("John", "First");
        User second = createSampleGreeting("Jane", "Second");
        User third = createSampleGreeting("Bob", "Third");

        // Then
        assertEquals("1", first.getId());
        assertEquals("2", second.getId());
        assertEquals("3", third.getId());
    }

    @Test
    @DisplayName("Test message format consistency")
    void testMessageFormatConsistency() {
        // Given
        String name = "John";
        String message = "Test message";

        // When
        User greeting = createSampleGreeting(name, message);

        // Then
        String expectedFormat = String.format("Hello %s! Your message: %s", name, message);
        assertEquals(expectedFormat, greeting.getMessage());
    }
}