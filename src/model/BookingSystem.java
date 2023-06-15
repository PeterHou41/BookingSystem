package src.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Booking System which stores information/states of
 * other four models.
 */
public class BookingSystem implements Serializable {

    private List<Building> buildings;
    private List<Room> rooms;
    private List<Person> people;
    private List<Booking> bookings;
    /** The text to store */
    private StringBuffer stringBuffer;
    private PropertyChangeSupport notifier;

    private boolean signalForRefreshingBefore;
    private boolean signalForRefreshingAfter;

    /** Instantiate a Booking System. */
    public BookingSystem() {
        buildings = new ArrayList<>();
        rooms = new ArrayList<>();
        people = new ArrayList<>();
        bookings = new ArrayList<>();
        stringBuffer = new StringBuffer();
        notifier = new PropertyChangeSupport(this);
        signalForRefreshingBefore = false;
        signalForRefreshingAfter = true;
    }
    /**
     * Permit and observer to add themselves as an observer to the model's change.
     * @param listener The listener to add.
     */
    public void addObserver(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    /**
     * Add a person with verified name an email address to the system.
     * @param name The name of the person.
     * @param email The email address of the person.
     */
    public void addPerson(String name, String email) {
        people.add(new Person(name, email));
    }

    /**
     * Add a building with verified name and address to the system.
     * @param name The name of the building.
     * @param address The address of the building.
     */
    public void addBuilding(String name, String address) {
        buildings.add(new Building(name, address));
    }

    /**
     * Add a room to the system and update the reference to the related building.
     * @param name The name of the room.
     * @param buildingOption The option typed by user to indicate which building this room located in.
     */
    public void addRoom(String name, int buildingOption) {

        Building building = buildings.get(buildingOption - 1);
        Room room = new Room(building);
        room.setName(name);
        building.addRoom(room);
        rooms.add(room);
        buildings.set(buildingOption - 1, building);

    }

    /**
     * Add a booking record to the system.
     * @param date Verified date.
     * @param startAndEnd The start and the end time of the booking.
     * @param personOption The option indicates the person who request the booking.
     * @param roomOption The option indicates the room which this booking reserved.
     */
    public void addBooking(LocalDate date, LocalTime[] startAndEnd, int personOption, int roomOption) {

        //Set the verified date and time and add the booking to the list.
        LocalTime verifiedStart = startAndEnd[0];
        LocalTime verifiedEnd = startAndEnd[1];
        Booking newBooking = new Booking(date, verifiedStart, verifiedEnd);

        // Add the reference to the person and the room.
        Room room = rooms.get(roomOption - 1);
        Person person = people.get(personOption - 1);
        newBooking.setPerson(person);
        newBooking.setRoom(room);
        person.addBooking(newBooking);
        room.addBooking(newBooking);
        people.set(personOption - 1, person);
        rooms.set(roomOption - 1, room);
        bookings.add(newBooking);

    }

    /**
     * Remove a person and all its related records/references from the system.
     * @param personOption The option indicates the person to be removed.
     */
    public void removePerson(int personOption) {
        Person person = people.get(personOption - 1);

        Booking booking;
        for (int bookingIndex = bookings.size() - 1; bookingIndex >= 0; bookingIndex--) {

            booking = bookings.get(bookingIndex);
            if (booking.getPerson().equals(person)) {
                for (Room room : rooms) {
                    room.removeBooking(booking); // Remove the reference of this booking from the room if stored.
                }
                bookings.remove(booking); // Then, remove the booking made by this person from the system.
            }
        }
        people.remove(personOption - 1);// Remove the person from the system.

    }

    /**
     * Remove a building and all its related records/references from the system.
     * @param buildingOption The option indicates the building to be removed.
     */
    public void removeBuilding(int buildingOption) {
        // Get the chosen building and delete its dependent instance/reference.
        Building building = buildings.get(buildingOption - 1);

        Room room;
        Booking booking;
        Person person;
        for (int roomIndex = rooms.size() - 1; roomIndex >= 0; roomIndex--) {

            room = rooms.get(roomIndex);
            if (room.getBuilding().equals(building)) {
                rooms.remove(room);
                for (int bookingIndex = bookings.size() - 1; bookingIndex >= 0; bookingIndex--) {
                    booking = bookings.get(bookingIndex);
                    if (booking.getRoom().equals(room)) {
                        for (int personIndex = people.size() - 1; personIndex >= 0; personIndex--) {
                            person = people.get(personIndex);
                            person.cancelBooking(booking); // Dereference the booking from person firstly.
                        }
                        bookings.remove(booking); // Safely remove the booking from bookings secondly.
                    }
                }
                rooms.remove(room); // Then, remove the room from rooms safely.
            }
        }
        buildings.remove(buildingOption - 1); // Finally, remove the building from the system.
    }


    /**
     * Remove a room and all its related records/references from the system.
     * @param roomOption The option indicates the room to be removed.
     */
    public void removeRoom(int roomOption) {
        // Get the chosen room and delete its transitive dependent instance/reference.
        Room room = rooms.get(roomOption - 1);

        Building building;

        // Firstly, dereference the room from its building.
        for (int buildingIndex = buildings.size() - 1; buildingIndex >= 0; buildingIndex--) {
            building = buildings.get(buildingIndex);
            if (building.getRooms().contains(room)) {
                building.removeRoom(room);
            }
        }

        Booking booking;
        Person person;

        for (int bookingIndex = bookings.size() - 1; bookingIndex >= 0; bookingIndex--) {
            booking = bookings.get(bookingIndex);
            if (booking.getRoom().equals(room)) {
                for (int personIndex = people.size() - 1; personIndex >= 0; personIndex--) {
                    person = people.get(personIndex);
                    person.cancelBooking(booking); // Dereference the booking from person firstly.
                }
                bookings.remove(booking); // Then, delete the booking records to this room safely.
            }
        }
        rooms.remove(roomOption - 1);// Finally, remove the room from the system.
    }


    /**
     * Remove a booking and all its related records/references from the system.
     * @param bookingOption The option indicates the booking to be removed.
     */
    public void removeBooking(int bookingOption) {

        Booking booking = bookings.get(bookingOption - 1);

        Person person;
        for (int personIndex = people.size() - 1; personIndex >= 0; personIndex--) {
            person = people.get(personIndex);
            if (person.getAllBookings().contains(booking)) {
                person.cancelBooking(booking); // Dereference the booking from the person.
                break; // A booking may only be requested by one person.
            }
        }

        Room room;
        Building building;

        for (int roomIndex = rooms.size() - 1; roomIndex >= 0; roomIndex--) {
            room = rooms.get(roomIndex);
            if (room.getAllBookings().contains(booking)) {
                for (int buildingIndex = buildings.size() - 1; buildingIndex >= 0; buildingIndex--) {
                    building = buildings.get(buildingIndex);
                    if (room.getBuilding().equals(building)) {
                        // Only execute once as the combination of the building and room is unique.
                        building.removeRoom(room); // Dereference the room with the reference for the booking.
                        room.removeBooking(booking); // Dereference the booking from the room.
                        building.addRoom(room); // Add the updated room back to this building, the order does not matter.
                        rooms.set(roomIndex, room); // Update the records of rooms.
                        buildings.set(buildingIndex, building); // Update the records of buildings.
                        break;
                    }
                }

            }
        }
        bookings.remove(bookingOption - 1);
    }


    /**
     * Check whether the user info entered is legitimate.
     * @param name Entered username to be verified.
     * @param email Entered email address to be verified.
     * @return Whether both verifications pass.
     */
    public boolean checkPersonInfoValidity(String name, String email) {
        boolean valid = true;
        // Check if empty name/email entered.
        if (name.equals("") || email.equals("")) {
            valid = false;
        }
        // Check if the entered email is unique.
        for (Person person : people) {
            if (person.getEmail().equals(email)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    /**
     * Check whether the building info entered is legitimate.
     * @param name Entered building name to be verified.
     * @param address Entered address to be verified.
     * @return Whether both verifications pass.
     */
    public boolean checkBuildingInfoValidity(String name, String address) {
        boolean valid = true;
        if (name.equals("") || address.equals("")) {
            valid = false;
        }
        for (Building building : buildings) {
            if (building.getAddress().equals(address)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    /**
     * Check whether the building info entered is legitimate.
     * @param name Entered room name to be verified.
     * @param buildingOption The option indicates the building in which the room is located.
     * @return Whether both verifications pass.
     */
    public boolean checkRoomInfoValidity(String name, int buildingOption) {
        boolean valid = true;
        // Check whether typed name is empty.
        if (name.equals("")) {
            valid = false;
        }
        Building building = buildings.get(buildingOption - 1);
        List<Room> roomsInThisBuilding = building.getRooms();
        // Check whether typed name already exist in the same building.
        for (Room room: roomsInThisBuilding) {
            if (room.getName().equals(name)) {
                valid = false;
                break;
            }
        }
        return valid;
    }


    /**
     * Public getter to get all people in the system.
     * @return All people.
     */
    public List<Person> getPeople() {
        return people;
    }

    /**
     * Public getter to get all buildings in the system.
     * @return All buildings.
     */
    public List<Building> getBuildings() {
        return buildings;
    }

    /**
     * Public getter to get all rooms in the system.
     * @return All systems.
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Public getter to get all bookings in the system.
     * @return All bookings.
     */
    public List<Booking> getBookings() {
        return bookings;
    }


    /**
     * Public method to realise the function of loading for this reference to two UIs.
     * @param bookingSystem An instance of booking system.
     */
    public void updatingSystem(BookingSystem bookingSystem) {
        people = bookingSystem.getPeople();
        buildings = bookingSystem.getBuildings();
        rooms = bookingSystem.getRooms();
        bookings = bookingSystem.getBookings();
    }

    /**
     * Invoked to update the text area in GUI.
     */
    public void notifyRefreshing() {
        notifier.firePropertyChange("ChangeConfirmation",
                signalForRefreshingBefore, signalForRefreshingAfter);
    }


}

