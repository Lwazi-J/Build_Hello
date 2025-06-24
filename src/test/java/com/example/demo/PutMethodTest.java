package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutMethodTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    /**
     * Helper method to create a sample greeting
     */
    private User createSampleGreeting(String name, String message) {
        UserRequest request = new UserRequest(name, message);
        return helloController.createGreeting(request);
    }

    /**
     * Test PUT endpoint updates existing greeting
     */
    @Test
    void testUpdateGreeting() {
        // Given
        User original = createSampleGreeting("John", "Initial message");
        String updatedName = "John Smith";
        String updatedMessage = "Updated message";

        // When
        UserRequest updateRequest = new UserRequest(updatedName, updatedMessage);
        ResponseEntity<User> response = helloController.updateGreeting(original.getId(), updateRequest);

        // Then
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Update should return successful status");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(original.getId(), response.getBody().getId(), "Updated greeting should maintain same ID");
        assertTrue(response.getBody().getMessage().contains(updatedName), "Updated greeting should contain new name");
        assertTrue(response.getBody().getMessage().contains(updatedMessage), "Updated greeting should contain new message");
    }
}