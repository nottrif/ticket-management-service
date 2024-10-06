package org.cloudbees.ticketmanagementservice.entity;

import lombok.Getter;

@Getter
public class Ticket {
    private String from;
    private String to;
    private double price;
    private User user;
    private String seat;
    private String section;

    public Ticket(User user, String seat, String section) {
        this.from = "London";
        this.to = "France";
        this.price = 20.0;
        this.user = user;
        this.seat = seat;
        this.section = section;
    }

    @Override
    public String toString() {
        return "Ticket [from=" + from + ", to=" + to + ", price=" + price + ", user=" + user + ", seat=" + seat
                + ", section=" + section + "]";
    }
}

