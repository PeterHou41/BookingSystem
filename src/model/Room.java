package src.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**Represents a model of Room.*/
public class Room implements Serializable {

    private String name;

    private Building building;
    private String building_located;
    private List<Booking> bookings;
    private PropertyChangeSupport notifier;

    /**
     * Instantiate a room with a building.
     * @param building The building which the room is in.
     */
    public Room(Building building) {
        this.building = building;
        bookings = new ArrayList<>();
        this.notifier = new PropertyChangeSupport(this);
    }

    /**
     * Register a listener, so it will be notified of any changes
     * @param listener
     */
    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }


    /**
     * Public method to set the name of the room.
     * @param name The name of the room.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Public getter to return the building in which this room is located.
     * @return The building this room is located in.
     */
    public Building getBuilding() {
        return building;
    }


    /**
     * Public method to add a reservation record to this room.
     * @param booking A booking(reservation) of this room.
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Public getter to return the name of the room.
     * @return The name of this room.
     */
    public String getName() {
        return name;
    }

    /**
     * Public getter to return all booking records of this room.
     * @return A list of booking records of this room.
     */
    public List<Booking> getAllBookings() {
        return bookings;
    }

    /**
     * Public method to remove a specific booking made for this rooom.
     * @param booking A booking made to be removed.
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    /**
     * Public method to print essential information about this room.
     * @return Info of the room in String.
     */
    public String getInfo() {
        return "\nName: " + name + "\nBuilding: " + building.getName() + "\n";
    }

    /**
     * Public getter to return the name of the building where this room is located.
     * @return Building's name.
     */
    public String getBuilding_located() {
        return building_located;
    }
}
