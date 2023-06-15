package src.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Represents a model of Person. */
public class Person implements Serializable {

    private String email;
    private String name;
    private List<Booking> allBookings;
    private PropertyChangeSupport notifier;


    /**
     * Instantiate a person with its name and email.
     * @param name The name of the person.
     * @param email The email address of the person.
     */
    public Person(String name, String email) {
        this.name = name;
        this.email = email;
        allBookings = new ArrayList<>();
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
     * Public getter to return the name of this person.
     * @return The name of this person.
     */
    public String getName() {
        return name;
    }

    /**
     * Public getter to return the email address of this person.
     * @return The email of this person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Public method to add a new booking to this person.
     * @param booking  A new booking to be added.
     */
    public void addBooking(Booking booking) {
        allBookings.add(booking);
    }

    /**
     * Public method to cancel an existed booking for this person.
     * @param booking A booking to cancel.
     */
    public void cancelBooking(Booking booking) {
        allBookings.remove(booking);
    }

    /**
     * Public getter to return all booking made by this person.
     * @return A list of booking made by this person.
     */
    public List<Booking> getAllBookings() {
        return allBookings;
    }

    /**
     * Dereference all bookings made by this person.
     */
    public void removeSelf() {
        allBookings.forEach(booking -> removeSelf());
    }



}
