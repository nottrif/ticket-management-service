package org.cloudbees.ticketmanagementservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cloudbees.ticketmanagementservice.entity.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDTO {
    private String from;
    private String to;
    private User user;
    private double price;
}
