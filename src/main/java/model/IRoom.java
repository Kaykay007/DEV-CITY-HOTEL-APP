package model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.security.auth.kerberos.KerberosTicket;

public interface IRoom {
    String getRoomNumber();
}
