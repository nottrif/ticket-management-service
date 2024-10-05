package org.cloudbees.ticketmanagementservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDTO {
    private String from;
    private String to;
    private UserDTO user;
    private double price;

//    @Override
//    public String toString() {
//        return "ReceiptDTO [from=" + from + ", to=" + to + ", user=" + user + ", price=" + price + "]";
//    }
}
