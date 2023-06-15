package src.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**Represents a model of Building.*/
public class Building implements Serializable {

    private String address;
    private String name;
    private List<Room> rooms;
    private PropertyChangeSupport notifier;

    /**
     * Create a building with its name and address.
     * @param name The name of this building.
     * @param address The geographical location of this building.
     */
    public Building(String name, String address) {
        this.address = address;
        this.name = name;
        rooms = new ArrayList<>();
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
     * Public getter to return the name of the building.
     * @return The name of this building.
     */
    public String getName() {
        return name;
    }

    /**
     * Public getter to return the address of the building.
     * @return The address of this building.
     */
    public String getAddress() {
        return address;
    }



    /**
     * Add a room to this building.
     * @param room Room added.
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * Remove a room for this building.
     * @param room Room removed.
     */
    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    /**
     * Public getter to return all rooms in this building.
     * @return All rooms in this building.
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Dereference the lower level dependent entities from this building.
     */
    public void removeSelf() {
        rooms.forEach(room -> removeSelf());
    }


}
