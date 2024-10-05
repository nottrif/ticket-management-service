package org.cloudbees.ticketmanagementservice.controller;

import org.cloudbees.ticketmanagementservice.DTOs.PurchaseTicketDTO;
import org.cloudbees.ticketmanagementservice.DTOs.ReceiptDTO;
import org.cloudbees.ticketmanagementservice.entity.Ticket;
import org.cloudbees.ticketmanagementservice.entity.User;
import org.cloudbees.ticketmanagementservice.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("api/v1/tickets")
public class TrainController {

    private final TrainService trainService;

    @Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @PostMapping("/purchase")
    public ResponseEntity purchaseTicket(@RequestBody PurchaseTicketDTO purchaseTicketDTO) {
        try {
            User user = new User(purchaseTicketDTO.getFirstName(), purchaseTicketDTO.getLastName(), purchaseTicketDTO.getEmail());
            ReceiptDTO receipt = trainService.purchaseTicket(user, purchaseTicketDTO.getSection(), purchaseTicketDTO.getSeat());
            return ResponseEntity.status(HttpStatus.CREATED).body(receipt);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @GetMapping("/receipt/{email}")
    public Ticket getReceipt(@PathVariable String email) {
        return trainService.getTicket(email);
    }

    @GetMapping("/seats")
    public List<String> getSeatsBySection(@RequestParam String section) {
        return trainService.getSeatsBySection(section);
    }

    @DeleteMapping("/remove/{email}")
    public String removeUser(@PathVariable String email) {
        return trainService.removeUser(email) ? "User removed successfully." : "User not found.";
    }

    @PutMapping("/modify-seat/{email}")
    public String modifySeat(@PathVariable String email, @RequestParam String newSection, @RequestParam String newSeat) {
        return trainService.modifySeat(email, newSection, newSeat) ? "Seat updated successfully." : "User not found.";
    }
}
