package model;

import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.security.auth.kerberos.KerberosTicket;
import java.util.Objects;

@AllArgsConstructor
@ToString

public class Room implements IRoom{



    private final String roomNumber;
    private final Double price;
    private final RoomType enumeration;
    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return getRoomNumber().equals(room.getRoomNumber()) && price.equals(room.price) && enumeration == room.enumeration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomNumber(), price, enumeration);
    }
}
