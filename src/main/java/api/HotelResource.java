package api;

import exception.CustomerAlreadyExistException;
import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {
    private final static HotelResource instance = new HotelResource();
    private final CustomerService customerService = CustomerService.getInstance();

    private final ReservationService reservationService = ReservationService.getInstance();

    public HotelResource() {
    }

    public static HotelResource getInstance() {
        return instance;
    }

    public Customer getCustomer(String email){
        return  customerService.getCust(email);
    }


    public  void createCustomerAccount(String email, String firstName, String lastName)
            throws IllegalArgumentException, CustomerAlreadyExistException {
        /** Code to create a customer account with the entered details */
        customerService.addCust(email, firstName, lastName);

    }




    public IRoom getRoom(String roomNumber){
        return reservationService.getRoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        return reservationService.reserveRoom(getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }
    public Collection<Reservation> getCustomersRoomReservations(String customerEmail){
        Customer customer = getCustomer(customerEmail);

        if(customer == null){
            return Collections.emptyList();
        }


        Collection<Reservation> HH= reservationService.getCustomerReservations(getCustomer(customerEmail));
        return HH==null?Collections.emptyList():HH;
    }
    public Collection<IRoom> findARoom(final Date checkIn, final Date checkOut){
        return reservationService.findHotelRooms(checkIn, checkOut);
    }

    public Collection<IRoom> searchForAlternativeRooms(final Date checkIn, final Date checkOut){
        return reservationService.searchOtherAlternateRooms(checkIn, checkOut);
    }


    public  Collection<IRoom> findAlternateDatesRoom ( Date checkIn, Date checkOut){
        ReservationService service = ReservationService.getInstance();
        return service.findHotelRooms(addDays(checkIn, 7), addDays(checkOut, 7));
    }

    public Date defaultPlusDays (final Date date){
        return reservationService.defaultPlusDays(date);
    }


    public static Date addDays(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}
