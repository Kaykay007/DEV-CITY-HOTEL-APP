package org.korede;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.Collection;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    private static final String ADMIN_MENU = """
        Admin menu
        ---------------------------------
        1. See all Customers
        2. View all Rooms
        3. See all Reservations
        4. Book a Room
        5. Back to Main Menu
        ---------------------------------
        Please select a number for the menu option:
        """;


    public static void adminMenu() {
        showMenu();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.length() == 1) {
                switch (input) {
                    case "1":
                        seeAllCustomers();
                        break;
                    case "2":
                        viewAllRooms();
                        break;
                    case "3":
                        seeAllReservations();
                        break;
                    case "4":
                        bookARoom();
                        break;
                    case "5":
                        MainMenu.mainMenu();
                        break;
                    default:
                        System.out.println("Invalid action");
                }
                showMenu();
            } else {
                System.out.println("Invalid action");
            }
        }
    }

    private static void showMenu() {
        System.out.println(ADMIN_MENU);
    }




    private static void bookARoom() {
        String roomId = readRoomId();
        double pricePerDay = readPricePerDay();
        RoomType roomType = readRoomType();

        adminResource.bookRoom(new Room(roomId, pricePerDay, roomType));

        askToBookAnotherRoom();
    }
    private static String readRoomId() {
        System.out.println("Input room Id:");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("\\d+")) {
                return input;
            } else {
                System.out.println("Invalid input. Please enter a number for the room ID.");
            }
        }
    }


    private static double readPricePerDay() {
        System.out.println("Input price per day:");
        return Double.parseDouble(scanner.nextLine().trim());
    }

    private static RoomType readRoomType() {
        while (true) {
            System.out.println("Input room type (SINGLE or DOUBLE):");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals(RoomType.SINGLE.toString())) {
                return RoomType.SINGLE;
            } else if (input.equals(RoomType.DOUBLE.toString())) {
                return RoomType.DOUBLE;
            }

            System.out.println("Invalid room type.");
        }
    }

    private static void askToBookAnotherRoom() {
        while (true) {
            System.out.println("Do you want to add another room? (Y/N)");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y")) {
                bookARoom();
                return;
            } else if (input.equals("N")) {
                return;
            }

            System.out.println("Invalid input. Please enter Y or N.");
        }
    }

    private static void seeAllReservations() {
        adminResource.showAllReservations();
    }

    private static void viewAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRoomsInTheHotel();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found");
            return;
        }
        for (IRoom room : rooms) {
            System.out.println(room);
        }
    }


private static void seeAllCustomers() {
    Collection<Customer> customers = adminResource.getAllHotelCustomers();
    if (customers.isEmpty()) {
        System.out.println("No customers found");
        return;
    }
    for (Customer customer : customers) {
        System.out.println(customer);
    }
}



}


