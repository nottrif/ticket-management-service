package org.cloudbees.ticketmanagementservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;

    @Override
    public String toString() {
        return "UserDTO [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
    }
}
