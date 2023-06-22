package src.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Booking implements Serializable {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String booker_name;
    private String room_name;
    private Person madeBy;
    private Room bookedAt;
    private PropertyChangeSupport notifier;


    /**
     * Instantiate an initial booking with the verified date and time.
     * @param date Date of the booking.
     * @param startTime Start time of the booking.
     * @param endTime End time of the booking.
     */
    public Booking(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
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
     * Set the person who made the booking.
     * @param person Person who made the booking.
     */
    public void setPerson(Person person) {
        madeBy = person;
    }

    /**
     * Set the room reserved by the booking.
     * @param room Room reserved by the booking.
     */
    public void setRoom(Room room) {
        bookedAt = room;
    }

    /**
     * Public getter to return the date of the booking.
     * @return The date of the booking.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Public getter to return the start time of the booking.
     * @return The start time of this booking.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Public getter to return the end time of the booking.
     * @return The end time of this booking.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Public getter to return the name of the person who made this booking.
     * @return The name of the person.
     */
    public String getPersonName() {
        return madeBy.getName();
    }

    /**
     * Public getter to return the person who made this booking.
     * @return Person that made this booking.
     */
    public Person getPerson() {
        return madeBy;
    }

    /**
     * Public getter to return the room reserved by this booking.
     * @return The room reserved by this booking.
     */
    public Room getRoom() {
        return bookedAt;
    }

    /**
     * Public method to print essential information about this booking.
     * @return Info of the booking in String.
     */
    public String getInfo() {
        return "\nPerson: " + madeBy.getName() + "\nDate: " + date.toString() +
                "\nStartTime: " + startTime.toString() + "\nEndTime: " + endTime.toString() +
               "\nBuilding: " + bookedAt.getBuilding().getName() + "\nRoom: " +  bookedAt.getName() + "\n";
    }

    /**
     * Public getter to return the booker's name.
     * @return Booker's name.
     */
    public String getBooker_name() {
        return booker_name;
    }

    /**
     * Public getter to return the reserved room's name.
     * @return Room's name.
     */
    public String getRoom_name() {
        return room_name;
    }

    /**
     * Public setter to set the booker's name of this booking.
     * @param booker_name Booker's name.
     */
    public void setBooker_name(String booker_name) {
        this.booker_name = booker_name;
    }

    /**
     * Public setter to set the reserved room's name of this booking.
     * @param room_name Room's name.
     */
    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

}
