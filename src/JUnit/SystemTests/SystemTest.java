package src.JUnit.SystemTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import src.model.*;

public class SystemTest {


    private BookingSystem bookingSystem;

    @Before
    public void SystemTest() {
        bookingSystem = new BookingSystem();
    }

    @Test
    public void testWrongPersonInfo() {
        boolean valid0 = bookingSystem.checkPersonInfoValidity("Joseph Biden", "bjohnson77@gov.uk");
        assertFalse(valid0);
        boolean valid1 = bookingSystem.checkPersonInfoValidity("", "whatever@testEmptyName");
        assertFalse(valid1);
    }

    @Test
    public void testCorrectPersonInfo() {
        boolean valid2 = bookingSystem.checkPersonInfoValidity("Donald Trump", "dtrump45@usa.gov");
        assertTrue(valid2);
    }

    @Test
    public void testEmptyBuildingInfo() {
        boolean valid3 = bookingSystem.checkBuildingInfoValidity("", "North Haugh");
        assertFalse(valid3);
        boolean valid4 = bookingSystem.checkBuildingInfoValidity("Jack Cole Building", "");
        assertFalse(valid4);
    }

    @Test
    public void testCorrectBuildingInfo() {
        boolean valid5 = bookingSystem.checkBuildingInfoValidity("Jack Cole Building", "North Haugh");
        assertTrue(valid5);
    }


}
