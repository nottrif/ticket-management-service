package org.cloudbees.ticketmanagementservice.service;

import org.cloudbees.ticketmanagementservice.DTOs.ReceiptDTO;
import org.cloudbees.ticketmanagementservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainServiceTest {

    private TrainService trainService;

    @BeforeEach
    void setUp() {
        trainService = new TrainService();
    }

    @Test
    void testPurchaseTicketSuccess() {
        User user = new User("John", "Doe", "john.doe@example.com");
        ReceiptDTO receipt = trainService.purchaseTicket(user, "A", "1");

        assertNotNull(receipt);
        assertEquals("John", receipt.getUser().getFirstName());
        assertEquals("Doe", receipt.getUser().getLastName());
    }

    @Test
    void testPurchaseTicketSeatTaken() {
        User user1 = new User("John", "Doe", "john.doe@example.com");
        User user2 = new User("Jane", "Smith", "jane.smith@example.com");

        trainService.purchaseTicket(user1, "A", "1");

        Executable executable = () -> trainService.purchaseTicket(user2, "A", "1");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Seat 1 in section A is not available.", exception.getMessage());
    }

    @Test
    void testPurchaseTicketAlreadyBooked() {
        User user = new User("John", "Doe", "john.doe@example.com");

        trainService.purchaseTicket(user, "A", "1");

        Executable executable = () -> trainService.purchaseTicket(user, "A", "2");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ticket has been booked for this email. Please use modify seat API to change your seat.", exception.getMessage());
    }

    @Test
    void testGetTicketSuccess() {
        User user = new User("John", "Doe", "john.doe@example.com");

        trainService.purchaseTicket(user, "A", "1");

        ReceiptDTO receipt = trainService.getTicket(user.getEmail());

        assertNotNull(receipt);
        assertEquals("John", receipt.getUser().getFirstName());
        assertEquals("Doe", receipt.getUser().getLastName());
    }

    @Test
    void testGetTicketNotFound() {
        ReceiptDTO receipt = trainService.getTicket("notfound@example.com");

        assertNull(receipt);
    }

    @Test
    void testRemoveUserSuccess() {
        User user = new User("John", "Doe", "john.doe@example.com");

        trainService.purchaseTicket(user, "A", "1");

        boolean removed = trainService.removeUser(user.getEmail());

        assertTrue(removed);

        assertTrue(trainService.getUsersBySection("A").isEmpty());
    }

    @Test
    void testRemoveUserNotFound() {
        boolean removed = trainService.removeUser("notfound@example.com");

        assertFalse(removed);
    }

    @Test
    void testModifySeatSuccess() {
        User user = new User("John", "Doe", "john.doe@example.com");

        trainService.purchaseTicket(user, "A", "1");

        ReceiptDTO receipt = trainService.modifySeat(user.getEmail(), "A", "2");

        assertNotNull(receipt);

        assertTrue(trainService.getUsersBySection("A").get("John Doe").contains("2"));
    }

    @Test
    void testModifySeatTaken() {
        User user1 = new User("John", "Doe", "john.doe@example.com");
        User user2 = new User("Jane", "Smith", "jane.smith@example.com");

        trainService.purchaseTicket(user1, "A", "1");
        trainService.purchaseTicket(user2, "A", "2");

        Executable executable = () -> trainService.modifySeat(user1.getEmail(), "A", "2");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Seat 2 in section A is not available.", exception.getMessage());
    }

    @Test
    void testModifySeatUserNotFound() {
        Executable executable = () -> trainService.modifySeat("notfound@example.com", "A", "2");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("No ticket found for user with email notfound@example.com", exception.getMessage());
    }

    @Test
    void testGetUsersBySection() {
        User user1 = new User("John", "Doe", "john.doe@example.com");
        User user2 = new User("Jane", "Smith", "jane.smith@example.com");

        trainService.purchaseTicket(user1, "A", "1");
        trainService.purchaseTicket(user2, "A", "2");

        Map<String, String> usersBySection = trainService.getUsersBySection("A");

        assertEquals(2, usersBySection.size());
        assertEquals("1", usersBySection.get("John Doe"));
        assertEquals("2", usersBySection.get("Jane Smith"));
    }
}