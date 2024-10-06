package org.cloudbees.ticketmanagementservice.controller;

import org.cloudbees.ticketmanagementservice.DTOs.PurchaseTicketDTO;
import org.cloudbees.ticketmanagementservice.DTOs.ReceiptDTO;
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
    public ResponseEntity getReceipt(@PathVariable String email) {
        ReceiptDTO receipt = trainService.getTicket(email);
        if (receipt != null) {
            return ResponseEntity.status(HttpStatus.OK).body(receipt);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found.");
        }
    }

    @GetMapping("/users-by-section")
    public ResponseEntity getUsersBySection(@RequestParam String section) {
        Map<String, String> usersBySection = trainService.getUsersBySection(section);
        if (usersBySection.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found in section " + section);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(usersBySection);
        }
    }

    @DeleteMapping("/remove/{email}")
    public ResponseEntity removeUser(@PathVariable String email) {
        return trainService.removeUser(email) ? ResponseEntity.status(HttpStatus.OK).body("User removed successfully.") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PutMapping("/modify")
    public ResponseEntity modifySeat(@RequestBody PurchaseTicketDTO purchaseTicketDTO) {
        try {
            ReceiptDTO receipt = trainService.modifySeat(purchaseTicketDTO.getEmail(), purchaseTicketDTO.getSection(), purchaseTicketDTO.getSeat());
            return ResponseEntity.status(HttpStatus.OK).body(receipt);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }
}
