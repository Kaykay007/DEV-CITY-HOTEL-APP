package org.korede;

import api.HotelResource;
import exception.CustomerAlreadyExistException;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import static api.HotelResource.addDays;

public class MainMenu{

    static HotelResource hotelResource = HotelResource.getInstance();
        private static Scanner scanner = new Scanner(System.in);

        public static void mainMenu() {
            showMainMenu();
            while (true) {
                String input = scanner.nextLine();
                if (input.length() != 1) {
                    System.out.println("Invalid input\n");
                    continue;
                }
                switch (input) {
                    case "1":
                        searchAndReserveRoom();
                        break;
                    case "2":
                        viewMyReservations();
                        break;
                    case "3":
                        createAccount();
                        break;
                    case "4":
                        AdminMenu.adminMenu();
                        break;
                    case "5":
                        System.out.println("Exiting program...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input\n");
                        break;
                }
                showMainMenu();
            }
        }

        private static void showMainMenu() {
            System.out.println("Welcome to the Hotel Reservation System!");
            System.out.println("1. find and reserve a room");
            System.out.println("2. see my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
        }

        private static void searchAndReserveRoom() {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("Enter Date to Check-In MM/dd/yyyy ");
                Date checkIn = getDate(scanner);

                System.out.println("Enter Date to Check-Out MM/dd/yyyy");
                Date checkOut = getDate(scanner);

                Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);
                if(availableRooms.isEmpty()){
                    Collection<IRoom> suggestedRooms =hotelResource.findAlternateDatesRoom(checkIn, checkOut);
                    if(suggestedRooms !=null && suggestedRooms.isEmpty()){
                        System.out.printf("Rooms not available for the given dates. : " +
                                        "we have the following rooms available with the suggested dates %s and %s \n",
                                addDays(checkIn, 7), addDays(checkOut, 7));
                        suggestedRooms.stream().forEach(room -> System.out.println(room));
                        System.out.println("Do you want to book a room with the suggested dates? Y/N");
                        String input = scanner.nextLine();
                        while (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")){
                            System.out.println("Invalid input. Please enter Y or N");
                            input = scanner.nextLine();
                        }
                        if (input.equalsIgnoreCase("Y")){
                            showRooms(suggestedRooms);
                            bookHotelRoom(suggestedRooms,scanner, checkIn, checkOut);
                        }
                        else if (input.equalsIgnoreCase("N")){
                            System.out.println("Thank you for using our application");
                        }
                    }
                }else {
                    showRooms(availableRooms);
                    bookHotelRoom(availableRooms,scanner, checkIn, checkOut);
                }
            } catch (IllegalArgumentException exception){
                System.out.println("Please enter a valid Date format MM/dd/yyyy");
            }
        }


        private static void viewMyReservations() {

            /**  Code to view the user's reservations **/
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input your Email address");
            String email = scanner.nextLine();
            Collection<Reservation> roomReservationList = HotelResource.getInstance().getCustomersRoomReservations(email);
            if(roomReservationList.isEmpty()){
                System.out.println("Reservation not found");
            }else {
                for (Reservation reservation : roomReservationList) {
                    System.out.println(reservation);
                }
            }
        }



    public static void createAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your email address:");
        String email = scanner.nextLine();

        System.out.println("Enter your first name:");
        String firstName = scanner.nextLine();

        System.out.println("Enter your last name:");
        String lastName = scanner.nextLine();

        try {
            /** Call the method to create a customer account with the entered details */
             hotelResource.createCustomerAccount(email, firstName, lastName);


            /** Display success message and show the main menu */
            System.out.println("User  has been created successfully");
            showMainMenu();
        } catch (IllegalArgumentException | CustomerAlreadyExistException e) {
            /** Display the error message and call the createAccount method again for the user to retry */
            System.out.println(e.getMessage());
            createAccount();
}
    }


    static void showRooms(Collection<IRoom> irooms){
        if(irooms.isEmpty()){
            System.out.println("Rooms not found");
        }else{
            for (IRoom room : irooms) {
                System.out.println(room);
            }

        }
    }

    static void bookHotelRoom(final Collection<IRoom> room, final Scanner scanner, final Date checkIn, final Date checkOut){
        System.out.println("Would you like to book a room? " +
                "Please enter Yes or No");
        try {
            String bookARoom = scanner.nextLine();
            if (bookARoom.substring(0,1).equalsIgnoreCase("y")){
                System.out.println("Do you have an account with us? y/n");
                String hasAnAccount = scanner.nextLine();
                if (hasAnAccount.substring(0,1).equalsIgnoreCase("y")){
                    System.out.println("Enter email address format: name@domain.com");
                    String email = scanner.nextLine();
                    if (hotelResource.getCustomer(email) == null){
                        System.out.println("Account with email " + email + " not found. Please create an account");
                        createAccount();
                    }else {
                        System.out.println("What room Id would you like to book");
                        final String roomId = scanner.nextLine();
                       if(room.stream().anyMatch(x -> x.getRoomNumber().equals(roomId))){
                            final IRoom iRoom = hotelResource.getRoom(roomId);
                            final Reservation reservation = hotelResource.
                                    bookARoom(email, iRoom, checkIn, checkOut);
                            System.out.println(reservation);
                           System.out.println("Room booked successfully");

                        }else {
                            System.out.println("Room has already been taken");
                        }
                    }
                    showMainMenu();
                }else {
                    System.out.println("Create an account with us");
                    showMainMenu();
                }
            }else if (bookARoom.substring(0,1).equalsIgnoreCase("n")){
                showMainMenu();
            }else {
                bookHotelRoom(room, scanner, checkIn, checkOut);
            }
        }catch (IllegalArgumentException exception){
            exception.getLocalizedMessage();
            System.out.println("Please provide a valid input!!! Yes or No");
        }
    }

    public static Date getDate(Scanner scanner) {
        System.out.println("Enter date in MM/dd/yyyy format:");
        String inputDate = scanner.nextLine();

        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(inputDate);
        } catch (ParseException exception) {
            System.out.println("Invalid date format. Please try again.");
            return getDate(scanner);
        }
    }
}
