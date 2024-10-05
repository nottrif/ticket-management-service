package org.cloudbees.ticketmanagementservice.controller;

import org.cloudbees.ticketmanagementservice.entity.Ticket;
import org.cloudbees.ticketmanagementservice.entity.User;
import org.cloudbees.ticketmanagementservice.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/train")
public class TrainController {

    private final TrainService trainService;

    @Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @PostMapping("/purchase")
    public Ticket purchaseTicket(@RequestBody Map<String, String> userData, @RequestParam String seat, @RequestParam String section) {
        User user = new User(userData.get("firstName"), userData.get("lastName"), userData.get("email"));
        return trainService.purchaseTicket(user, seat, section);
    }

    @GetMapping("/receipt/{email}")
    public Ticket getReceipt(@PathVariable String email) {
        return trainService.getTicket(email);
    }

    @GetMapping("/seats")
    public Map<String, List<Ticket>> getSeatsBySection(@RequestParam String section) {
        return trainService.getSeatsBySection(section);
    }

    @DeleteMapping("/remove/{email}")
    public String removeUser(@PathVariable String email) {
        return trainService.removeUser(email) ? "User removed successfully." : "User not found.";
    }

    @PutMapping("/modify-seat/{email}")
    public String modifySeat(@PathVariable String email, @RequestParam String newSeat) {
        return trainService.modifySeat(email, newSeat) ? "Seat updated successfully." : "User not found.";
    }
}
