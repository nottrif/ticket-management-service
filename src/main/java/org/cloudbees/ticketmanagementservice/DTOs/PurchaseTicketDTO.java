package org.cloudbees.ticketmanagementservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseTicketDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String seat;
    private String section;
}
