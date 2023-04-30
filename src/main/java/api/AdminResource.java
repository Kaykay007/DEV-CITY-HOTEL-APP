package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;

public class AdminResource {

    private static final AdminResource instance = new AdminResource();

    private final CustomerService customerService = CustomerService.getInstance();

    private final ReservationService reservationService =  ReservationService.getInstance();

    public AdminResource() {

    }

    public static AdminResource getInstance(){
        return instance;
    }

    public void bookRoom(IRoom room){
        reservationService.bookRoom(room);
    }

    public Collection<IRoom> getAllRoomsInTheHotel(){
        return reservationService.getAllHotelRooms();
    }

    public Collection<Customer> getAllHotelCustomers(){
        return customerService.getAllCustomers();
    }

    public void showAllReservations(){
        reservationService.printAllRoomReservations();
    }


}
