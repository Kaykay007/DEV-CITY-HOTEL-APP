package model;

import lombok.*;

import java.util.regex.Pattern;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Customer {

    private final String firstName;
    private final String lastName;
    private final String email;

    private static final String mailRegex = "^(.+)@(.+).(.+)$";


    private void mailPatternMatches(String email) {
        if (Pattern.compile(Customer.mailRegex).matcher(email).matches()) {
            System.out.println("This is a valid  Email");
        } else {
            throw new IllegalArgumentException("This is an Invalid Email");
        }
    }


}
