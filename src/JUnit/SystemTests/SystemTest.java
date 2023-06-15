package src.JUnit.SystemTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import src.model.*;

import java.util.List;

public class SystemTest {


    private BookingSystem bookingSystem;

    @Before
    public void SystemTest() {
        bookingSystem = new BookingSystem();
        bookingSystem.addBuilding(
                "John Honey Building", "University of, John Honey Building, N Haugh, St Andrews KY16 9SX");
    }

    @Test
    public void testInvalidPersonInfo() {
        boolean valid0 = bookingSystem.checkPersonInfoValidity("Joseph Biden", "");
        assertFalse(valid0);
        boolean valid1 = bookingSystem.checkPersonInfoValidity("", "whatever@testEmptyName");
        assertFalse(valid1);
    }

    @Test
    public void testValidPersonInfo() {
        boolean valid0 = bookingSystem.checkPersonInfoValidity("Boris Johnson", "bjohnson77@gov.uk");
        assertTrue(valid0);
        boolean valid1 = bookingSystem.checkPersonInfoValidity("Donald Trump", "dtrump45@usa.gov");
        assertTrue(valid1);
    }

    @Test
    public void testInvalidBuildingInfo() {
        boolean invalid0 = bookingSystem.checkBuildingInfoValidity("", "North Haugh");
        assertFalse(invalid0);
        boolean invalid1 = bookingSystem.checkBuildingInfoValidity("Jack Cole Building", "");
        assertFalse(invalid1);
    }

    @Test
    public void testValidBuildingInfo() {
        boolean valid0 = bookingSystem.checkBuildingInfoValidity(
                "Jack Cole Building", "Jack Cole Building, N Haugh, St Andrews KY16 9SX");
        assertTrue(valid0);
        boolean valid1 = bookingSystem.checkBuildingInfoValidity("PM's Office", "10 Downing Street");
        assertTrue(valid1);
    }

    // John Honey Building was already added to the system.
    @Test
    public void testValidRoomInfo() {

        boolean valid0 = bookingSystem.checkRoomInfoValidity("JH109 Quiet Lab", 1);
        assertTrue(valid0);
        boolean valid1 = bookingSystem.checkRoomInfoValidity("JH110 Teaching Lab", 1);
        assertTrue(valid1);
    }

    @Test
    public void testInvalidRoomInfo() {

        boolean invalid0 = bookingSystem.checkRoomInfoValidity("", 1);
        assertFalse(invalid0);

        bookingSystem.addRoom("JH109 Quiet Lab", 1);
        boolean invalid1 = bookingSystem.checkRoomInfoValidity("JH109 Quiet Lab", 1);
        assertFalse(invalid1);
    }

    @Test
    public void testPeopleNum() {
        bookingSystem.addPerson("Boris Johnson", "bjohnson77@gov.uk");
        bookingSystem.addPerson("Donald Trump", "dtrump45@usa.gov");

        List<Person> people = bookingSystem.getPeople();
        boolean valid0 = (people.size() == 2);
        assertTrue(valid0);

        bookingSystem.removePerson(2);
        bookingSystem.addPerson("Rowan Atkinson", "mrbean91@gmail.com");
        boolean valid1 = (people.size() == 2);
        assertTrue(valid1);
    }

    // John Honey Building was already added to the system.
    @Test
    public void testBuildingNum() {
        bookingSystem.addBuilding("Younger Hall", "St Andrews North Street KY16 9AJ");
        bookingSystem.addBuilding(
                "University Sports Centre",
                "Saints Sport, University Park, St Leonard's Rd, St Andrews KY16 9DY");

        List<Building> buildings = bookingSystem.getBuildings();
        boolean valid0 = (buildings.size() == 3);
        assertTrue(valid0);

        bookingSystem.removeBuilding(2);
        boolean valid1 = (buildings.size() == 2);
        assertTrue(valid1);
    }

    @Test
    public void testRoomNum() {

        bookingSystem.addRoom("JH109 Quiet Lab", 1);
        bookingSystem.addRoom("JH110 Teaching Lab", 1);
        bookingSystem.addRoom("JH108 PhD Candidates Offices", 1);

        List<Room> rooms = bookingSystem.getRooms();
        boolean valid0 = (rooms.size() == 3);
        assertTrue(valid0);

        bookingSystem.removeRoom(2);
        boolean valid1 = (rooms.size() == 2);
        assertTrue(valid1);
    }




}
