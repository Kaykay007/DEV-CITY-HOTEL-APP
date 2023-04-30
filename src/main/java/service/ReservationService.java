package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
public class ReservationService {
    private static final ReservationService instance = new ReservationService();
   private static  List<Reservation> customerReservationList = new ArrayList<>();
    private CustomerService customerService;
   private static Map<String, IRoom> rooms;
    private Map<String, List<Reservation>> appConfigMap;


    private ReservationService() {
        customerService = CustomerService.getInstance();
        rooms = new HashMap<>();
        appConfigMap = new HashMap<>();
    }



    public static ReservationService getInstance() {
        return instance;
    }

    public void bookRoom(final IRoom room) {
        if (!rooms.containsKey(room.getRoomNumber())){
            rooms.put(room.getRoomNumber(), room);
            System.out.println("Room booked successfully");
        } else {
            System.out.println("This room is already booked.");
        }
    }

    public IRoom getRoom(final String roomNumber) {
        return rooms.get(roomNumber);
    }

    public Reservation reserveRoom(final Customer customer, final IRoom room, final Date checkInDate, final Date checkOutDate) {
        final Reservation reserve = new Reservation(customer, room, checkInDate, checkOutDate);
        Map<String, Customer> customersList = customerService.customerMap;


        try {
            if (customersList.containsKey(customer.getEmail())) {
                if (rooms.containsKey(room.getRoomNumber())) {
                    if (isRoomAvailable(checkInDate, checkOutDate, (Room) reserve.getRoom())) {
                        customerReservationList.add(reserve);
                        if (appConfigMap.containsKey(customer.getEmail())) {
                            appConfigMap.get(customer.getEmail()).add(reserve);
                        } else {
                            appConfigMap.put(customer.getEmail(), new ArrayList<>(Collections.singletonList(reserve)));
                        }
                        return reserve;
                    } else {
                        System.out.println("Room has been reserved by a customer,kindly check for the next availability");
                        System.out.println(searchOtherAlternateRooms(checkInDate, checkOutDate));
                    }
                } else {
                    System.out.println("No such room available. see room numbers" + room.getRoomNumber());
                }
            } else {
                System.out.println("No customer found with email " + customer.getEmail());
            }
        } catch (NullPointerException exception) {
            exception.getLocalizedMessage();
        }
        return reserve;
    }

    public Collection<IRoom> findHotelRooms(final Date checkIn, final Date checkOut) {
        return searchOtherAvailableRooms(checkIn, checkOut);
    }

    public  List<Reservation> getCustomerReservations(final Customer customer) {
        return appConfigMap.get(customer.getEmail());
    }

    /** This function prints out a list of reservations that have been made for a hotel's rooms */
    public void printAllRoomReservations() {

        /** Get a collection of all the reservations that have been made for the hotel's rooms*/
        Collection<Reservation> reservations = takenRooms();

        /**If there are no reservations in the collection, print a message saying no reservations have been made */
        if (reservations.isEmpty()) {
            System.out.println("No reservations have been made");
        }
        /** Otherwise, print out each reservation one by one on a new line */
        else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation + "\n");
            }
        }
    }


    public Collection<IRoom> searchOtherAlternateRooms(final Date checkIn, Date checkOut) {
        return searchOtherAvailableRooms(defaultPlusDays(checkIn), defaultPlusDays(checkOut));
    }


    private Collection<IRoom> searchOtherAvailableRooms(final Date checkIn, final Date checkOut) {
        Collection<Reservation> bookedRooms = takenRooms();
        Set<IRoom> otherAvailableRooms =  rooms.values().stream().collect(Collectors.toSet());

        for (Reservation reservation : bookedRooms) {
            if (!isRoomAvailable(checkIn, checkOut, (Room) reservation.getRoom())) {
                /** Skip this room and move on to the next one **/
                IRoom room = reservation.getRoom();
                otherAvailableRooms.remove(room);
              //  otherAvailableRooms.add(room);
            }


        }
        return otherAvailableRooms;

//        return rooms.values()
//                .stream()
//                .filter(room -> !otherAvailableRooms.contains(room))
//                .collect(Collectors.toList());
    }


    public Collection<Reservation> takenRooms() {
        Collection<List<Reservation>> takenRooms = appConfigMap.values();
        Collection<Reservation> currentlyTakenRooms = new LinkedList<>();
        for (List<Reservation> reservationList : takenRooms) {
            currentlyTakenRooms.addAll(reservationList);
        }
        return currentlyTakenRooms;
    }


    private boolean isRoomAvailable(final Date checkIn, final Date checkOut, final Room room) {
//        Date reservationCheckIn = reservation.getCheckInDate();
//        Date reservationCheckOut = reservation.getCheckOutDate();

        boolean isAvailable = true ;
        for (Map.Entry<String, List<Reservation>> entry: appConfigMap.entrySet()) {
            List<Reservation> reservations = entry.getValue();
            for (Reservation reservation : reservations) {
                if (reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                    Date reservationCheckIn = reservation.getCheckInDate();
                    Date reservationCheckOut = reservation.getCheckOutDate();
                    if (checkIn.before(reservationCheckIn) && checkOut.before(reservationCheckIn)) {
                        isAvailable = true;
                    } else if (checkIn.after(reservationCheckOut) && checkOut.after(reservationCheckOut)) {
                        isAvailable = true;
                    } else {
                        isAvailable = false;
                    }
                }
            }
        }
        return isAvailable;
    }


    public Date defaultPlusDays(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
    public Collection<IRoom> getAllHotelRooms() {
        return rooms.values();
    }


    public void addRoom(String valueOf) {
    }
}
