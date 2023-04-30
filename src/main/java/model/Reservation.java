package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import java.util.Date;

@AllArgsConstructor
@Data
@ToString
public class Reservation {

    private final  Customer customer;
    private final  IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

}
