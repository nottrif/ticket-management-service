package org.cloudbees.ticketmanagementservice.service;

import org.cloudbees.ticketmanagementservice.DTOs.ReceiptDTO;
import org.cloudbees.ticketmanagementservice.entity.Ticket;
import org.cloudbees.ticketmanagementservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing train ticket operations.
 */
@Service
public class TrainService {
    private List<String> sectionASeats = new ArrayList<>();
    private List<String> sectionBSeats = new ArrayList<>();
    private Map<String, Ticket> allocatedTickets = new HashMap<>();

    /**
     * Initializes the TrainService with seats for sections A and B.
     * Seats are numbered from A1 to A50 and B1 to B50.
     */
    public TrainService() {
        for (int i = 1; i <= 50; i++) {
            sectionASeats.add("A" + i);
            sectionBSeats.add("B" + i);
        }
    }

    /**
     * Purchases a ticket for a user.
     *
     * @param user    the user purchasing the ticket
     * @param section the section of the seat
     * @param seat    the seat number
     * @return a ReceiptDTO containing the ticket details
     * @throws IllegalArgumentException if the seat is not available or the user already has a ticket
     */
    public ReceiptDTO purchaseTicket(User user, String section, String seat) {
        if (allocatedTickets.containsKey(user.getEmail())) {
            throw new IllegalArgumentException("Ticket has been booked for this email. Please use modify seat API to change " +
                    "your seat.");
        }

        String chosenSeat = section.concat(seat);
        if ("A".equals(section) && sectionASeats.contains(chosenSeat)) {
            sectionASeats.remove(chosenSeat);
        } else if ("B".equals(section) && sectionBSeats.contains(chosenSeat)) {
            sectionBSeats.remove(chosenSeat);
        } else {
            throw new IllegalArgumentException("Seat " + seat + " in section " + section + " is not available.");
        }

        Ticket ticket = new Ticket(user, seat, section);
        allocatedTickets.put(user.getEmail(), ticket);

        return new ReceiptDTO(ticket.getFrom(), ticket.getTo(), user, ticket.getPrice());
    }

    /**
     * Retrieves the ticket for a user by email.
     *
     * @param email the email of the user
     * @return a ReceiptDTO containing the ticket details, or null if no ticket is found
     */
    public ReceiptDTO getTicket(String email) {
        Ticket ticket = allocatedTickets.get(email);
        if (ticket != null) {
            return new ReceiptDTO(ticket.getFrom(), ticket.getTo(), ticket.getUser(), ticket.getPrice());
        } else {
            return null;
        }
    }

    /**
     * Retrieves users and their allocated seats by section.
     *
     * @param section the section to filter by
     * @return a map of user names to seat numbers
     */
    public Map<String, String> getUsersBySection(String section) {
        return allocatedTickets.values().stream()
                .filter(ticket -> section.equals(ticket.getSection()))
                .collect(Collectors.toMap(ticket -> ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName(),
                        Ticket::getSeat));
    }

    /**
     * Removes a user and their ticket by email.
     *
     * @param email the email of the user
     * @return true if the user was removed, false otherwise
     */
    public boolean removeUser(String email) {
        Ticket ticket = allocatedTickets.remove(email);
        if (ticket != null) {
            String seat = ticket.getSection().concat(ticket.getSeat());
            if ("A".equals(ticket.getSection())) {
                sectionASeats.add(seat);
                Collections.sort(sectionASeats);
            } else if ("B".equals(ticket.getSection())) {
                sectionBSeats.add(seat);
                Collections.sort(sectionBSeats);
            }
            return true;
        }
        return false;
    }

    /**
     * Modifies the seat for a user's ticket.
     *
     * @param email     the email of the user
     * @param newSection the new section of the seat
     * @param newSeat   the new seat number
     * @return a ReceiptDTO containing the updated ticket details
     * @throws IllegalArgumentException if the new seat is not available or no ticket is found for the user
     */
    public ReceiptDTO modifySeat(String email, String newSection, String newSeat) {
        Ticket ticket = allocatedTickets.get(email);
        if (ticket == null) {
            throw new IllegalArgumentException("No ticket found for user with email " + email);
        }

        String seat = newSection.concat(newSeat);
        if ("A".equals(newSection) && sectionASeats.contains(seat)) {
            sectionASeats.remove(seat);
        } else if ("B".equals(newSection) && sectionBSeats.contains(seat)) {
            sectionBSeats.remove(seat);
        } else {
            throw new IllegalArgumentException("Seat " + newSeat + " in section " + newSection + " is not available.");
        }

        String oldSeat = ticket.getSection().concat(ticket.getSeat());
        if ("A".equals(ticket.getSection())) {
            sectionASeats.add(oldSeat);
            Collections.sort(sectionASeats);
        } else if ("B".equals(ticket.getSection())) {
            sectionBSeats.add(oldSeat);
            Collections.sort(sectionBSeats);
        }

        Ticket newTicket = new Ticket(ticket.getUser(), newSeat, newSection);
        allocatedTickets.put(email, newTicket);

        return new ReceiptDTO(newTicket.getFrom(), newTicket.getTo(), newTicket.getUser(), newTicket.getPrice());
    }
}