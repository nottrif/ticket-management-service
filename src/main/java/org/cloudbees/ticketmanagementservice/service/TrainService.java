package org.cloudbees.ticketmanagementservice.service;

import org.cloudbees.ticketmanagementservice.entity.Ticket;
import org.cloudbees.ticketmanagementservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainService {
    private Map<String, List<Ticket>> sectionA = new HashMap<>();
    private Map<String, List<Ticket>> sectionB = new HashMap<>();

    public TrainService() {
        sectionA.put("A", new ArrayList<>());
        sectionB.put("B", new ArrayList<>());
    }

    public Ticket purchaseTicket(User user, String seat, String section) {
        Ticket ticket = new Ticket(user, seat, section);

        if ("A".equals(section)) {
            sectionA.get("A").add(ticket);
        } else if ("B".equals(section)) {
            sectionB.get("B").add(ticket);
        }
        return ticket;
    }

    public Ticket getTicket(String email) {
        List<Ticket> allTickets = new ArrayList<>();
        allTickets.addAll(sectionA.get("A"));
        allTickets.addAll(sectionB.get("B"));

        return allTickets.stream().filter(t -> t.getUser().getEmail().equals(email)).findFirst().orElse(null);
    }

    public Map<String, List<Ticket>> getSeatsBySection(String section) {
        if ("A".equals(section)) {
            return sectionA;
        } else {
            return sectionB;
        }
    }

    public boolean removeUser(String email) {
        return sectionA.get("A").removeIf(t -> t.getUser().getEmail().equals(email)) ||
                sectionB.get("B").removeIf(t -> t.getUser().getEmail().equals(email));
    }

    public boolean modifySeat(String email, String newSeat) {
        Ticket ticket = getTicket(email);
        if (ticket != null) {
            ticket.getSeat().replace(ticket.getSeat(), newSeat);
            return true;
        }
        return false;
    }
}

