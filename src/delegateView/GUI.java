package src.delegateView;

import src.model.*;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;


import javax.swing.*;

import static src.delegateView.SharedConstants.*;

/** Represents a graphical UI. */
public class GUI extends JFrame implements PropertyChangeListener{

    private BookingSystem bookingSystem;
    private JButton optionsButton;
    private JButton logicButton;
    private JButton clearAll;
    private JFrame mainFrame;
    private JFileChooser fileChooser;

    private JTextArea outputField;
    private JToolBar toolBar;
    private JMenuBar menuBar;
    private JMenu addMenu;
    private JMenu removeMenu;
    private JMenu viewMenu;
    private JMenu fileMenu;

    private JMenuItem loadItem;
    private JMenuItem saveItem;
    private JMenuItem addPersonItem;
    private JMenuItem addBuildingItem;
    private JMenuItem addRoomItem;
    private JMenuItem addBookingItem;
    private JMenuItem removePersonItem;
    private JMenuItem removeBuildingItem;
    private JMenuItem removeRoomItem;
    private JMenuItem removeBookingItem;
    private JMenuItem allRoomAtATime;
    private JMenuItem allRoomAtATimeBlock;
    private JMenuItem allBookingByAPerson;
    private JMenuItem allScheduleForARoom;
    private JMenuItem allModels;


    private JScrollPane outputPane;
    private StringBuffer stringBuffer;



    private static final int FRAME_HEIGHT_AND_WIDTH = 600;


    /**
     * Instantiate a GUI for the Simple Booking System.
     * @param sharedSystem Model of the booking system shared by GUI and TUI.
     */
    public GUI(BookingSystem sharedSystem) {
            bookingSystem = sharedSystem;
            mainFrame = new JFrame("Simple Room Booking System");
            menuBar = new JMenuBar();
            outputField = new JTextArea();
            outputField.setEditable(false);
            toolBar = new JToolBar();
            outputPane = new JScrollPane(outputField);
            stringBuffer = new StringBuffer();

            setupComponents();
            bookingSystem.addObserver(this);

    }



    private void setUpMenu() {
        addMenu = new JMenu("Add");
        removeMenu = new JMenu("Remove");
        viewMenu = new JMenu("View");
        fileMenu = new JMenu("File");

        loadItem = new JMenuItem ("Load");
        saveItem = new JMenuItem ("Save");
        implementLoadOrSave(true, loadItem);
        implementLoadOrSave(false, saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);

        menuBar.add(addMenu);
        menuBar.add(removeMenu);
        menuBar.add(viewMenu);
        menuBar.add(fileMenu);

        mainFrame.setJMenuBar(menuBar);

        addFunctionsToView(viewMenu);
        addFunctionsToRemove(removeMenu);
        addFunctionsToAdd(addMenu);

    }

    private void implementLoadOrSave(boolean isLoad, JMenuItem loadOrSave) {
        fileChooser = new JFileChooser("./src/logs/");

        if (isLoad) {
            // Load
            loadOrSave.addActionListener(e -> {
                fileChooser.setDialogTitle("Load System State File");
                int returnVal = fileChooser.showOpenDialog(mainFrame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File underlyingFile = fileChooser.getSelectedFile();
                    try {
                        FileInputStream fis = new FileInputStream(underlyingFile);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        bookingSystem.updatingSystem((BookingSystem) ois.readObject());
                        fis.close();
                        ois.close();
                        popMsgToMainFrame("\nSystem state loaded from file " +
                                underlyingFile.toString() +
                                " successfully!\n", "Successful", 1);
                        bookingSystem.notifyRefreshing();
                    }
                    catch (Exception exception) {
                        popMsgToMainFrame(
                                "Loading interrupted for unknown reason!", "Loading Failed", 2);
                        exception.printStackTrace();
                    }
                }
                else {
                    popMsgToMainFrame("User didn't select and press 'Open' button," +
                            "loading failed!", "Halt", 2);
                }
            });

        }
        else {
            //Save
            loadOrSave.addActionListener(e -> {
                fileChooser.setDialogTitle("Save System State File");
                int returnVal = fileChooser.showSaveDialog(mainFrame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File underlyingFile = fileChooser.getSelectedFile();
                    try {
                        FileOutputStream fos = new FileOutputStream(underlyingFile);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(bookingSystem);
                        fos.close();
                        oos.close();
                        popMsgToMainFrame("\nSystem state saved to file " +
                                        underlyingFile.toString() +
                                        " successfully!\n", "Successful", 1);
                        bookingSystem.notifyRefreshing();
                    }
                    catch (Exception exception) {
                        popMsgToMainFrame(
                                "Saving interrupted for unknown reason!", "Saving Failed", 2);
                        exception.printStackTrace();
                    }
                }
                else {
                    popMsgToMainFrame("User didn't select and press 'Save' button," +
                            "saving canceled!", "Halt", 2);
                }
            });

        }
    }

    private void implementHelpContent(boolean isOption, JButton optionOrLogic) {

        if (isOption) {
            optionOrLogic.addActionListener( e -> {
                JOptionPane.showMessageDialog(mainFrame, graphicalHelp);
            });
        }
        else {
            optionOrLogic.addActionListener(e -> {
                JOptionPane.showMessageDialog(mainFrame, logic);
            });

        }
    }


    private void addFunctionsToAdd(JMenu addMenu) {

        addPersonItem = new JMenuItem ("Person");
        addBuildingItem = new JMenuItem ("Building");
        addRoomItem = new JMenuItem ("Room");
        addBookingItem = new JMenuItem ("Booking");

        addPersonItem.addActionListener(e -> {
            try {
                printNewMsg("Please enter the name of the person: ");
                String name = getInputString();

                printNewMsg("Please enter the email address of the person: ");
                String email = getInputString();
                boolean valid = bookingSystem.checkPersonInfoValidity(name, email);
                if (valid) {
                    bookingSystem.addPerson(name, email);
                    printNewMsg("Registration of <" + name +
                            "> with a unique email address <" + email +
                            "> is successful!");
                }
                else {
                    popMsgToMainFrame("Repetitive email or invalid name detected, registration failed",
                            "Error", 0);
                }
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
        });

        addBuildingItem.addActionListener(e -> {
            try {
                printNewMsg("Please enter the name of the building: ");
                String name = getInputString();
                printNewMsg("Please enter the address of the building: ");
                String address = getInputString();

                boolean valid = bookingSystem.checkBuildingInfoValidity(name, address);
                if (valid) {
                    bookingSystem.addBuilding(name, address);
                    printNewMsg("Registration of <" + name +
                            "> with a physical location <" + address +
                            "> is successful!\n");
                }
                else {
                    popMsgToMainFrame("Invalid name or address detected, registration failed",
                            "Error", 0);
                }
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
        });

        addRoomItem.addActionListener(e -> {
            try {
                printNewMsg("Which building you would like this room located in?");
                printCurrentBuildings(bookingSystem.getBuildings());

                int chosenBuilding = Integer.parseInt(getInputString());

                printNewMsg("Please enter the name of the Room: ");
                String name = getInputString();
                boolean valid = bookingSystem.checkRoomInfoValidity(name, chosenBuilding);
                if (!valid) {
                    popMsgToMainFrame("Empty name detected or repetitive name detected" +
                                    " in the same building, room registration failed!",
                            "Error", 0);
                    return;
                }
                bookingSystem.addRoom(name, chosenBuilding);
                printNewMsg("Registration for <" + name + "> with the building chosen is successful!\n");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
        });

         addBookingItem.addActionListener(e -> {
            try {
                // Get Person and Room
                printNewMsg("Which person is requesting a booking?");
                printCurrentPeople(bookingSystem.getPeople());
                printNewMsg("Enter your option, '0' to interrupt the action: ");
                int chosenPerson = Integer.parseInt(getInputString());
                if (chosenPerson == 0) {
                    printNewMsg("Action interrupted!");
                    return;
                }

                printNewMsg("Which room would be booked?");
                printCurrentRooms(bookingSystem.getRooms());
                printNewMsg("Enter your option, '0' to interrupt the action: ");
                int chosenRoom = Integer.parseInt(getInputString());
                if (chosenRoom == 0) {
                    printNewMsg("Action interrupted!");
                    return;
                }

                // Date and Time legality check.
                printNewMsg("The entered date and time must be no earlier than now(when sending the request).");
                printNewMsg("Please enter the date for the booking.");
                LocalDate underlyingDate = dateFormatCheck();
                if (underlyingDate == null) {
                    return;
                }
                boolean isToday = underlyingDate.equals(LocalDate.now());
                printNewMsg("Please enter the start and the end time for the booking.");
                LocalTime[] underlyingStartAndEnd = bookingTimeFormatCheck(isToday);
                if (underlyingStartAndEnd == null) {
                    return;
                }
                List<Room> rooms = bookingSystem.getRooms();
                Room underlyingRoom = rooms.get(chosenRoom - 1);


                //Booking conflict check.
                if (bookingConflictCheck(underlyingRoom, underlyingDate, underlyingStartAndEnd)) {
                    return;
                }

                bookingSystem.addBooking(underlyingDate, underlyingStartAndEnd, chosenPerson, chosenRoom);
                printNewMsg("Booking registered successfully!");

            }
            catch (DateTimeParseException parseException) {
                popMsgToMainFrame("Input Date or Time parsing failed!", "Error", 0);
            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }

        });

        addMenu.add(addPersonItem);
        addMenu.add(addBuildingItem);
        addMenu.add(addRoomItem);
        addMenu.add(addBookingItem);

    }

    private void addFunctionsToRemove(JMenu removeMenu) {
        removePersonItem = new JMenuItem ("Person");
        removeBuildingItem = new JMenuItem ("Building");
        removeRoomItem = new JMenuItem ("Room");
        removeBookingItem = new JMenuItem ("Booking");


        removePersonItem.addActionListener(e -> {
            try {
                printNewMsg("Which person would you like to remove from the system?");
                printCurrentPeople(bookingSystem.getPeople());
                printNewMsg("\n### WARNING ###\nOnce confirmed, all bookings made by" +
                        "\nthis person would be removed,");
                printNewMsg("Enter your option, '0' to interrupt the action: ");

                int chosenPerson = Integer.parseInt(getInputString());
                if (chosenPerson == 0) {
                    printNewMsg("Action interrupted!");
                    return;
                }

                // Get the chosen person and delete its dependent instance/reference.
                bookingSystem.removePerson(chosenPerson);
                printNewMsg("Records and the corresponding references removed successfully!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }
        });
        removeBuildingItem.addActionListener(e -> {
            try{
                printNewMsg("Which building would you like to remove from the system?");
                printCurrentBuildings(bookingSystem.getBuildings());
                printNewMsg("\n### WARNING ###\nOnce confirmed, all rooms located in this building and all " +
                        "\nsubsequent booking records and their reference for person would also be removed!");
                printNewMsg("Enter your option, '0' to interrupt the action: ");

                int chosenBuilding = Integer.parseInt(getInputString());
                if (chosenBuilding == 0) {
                    printNewMsg("Action interrupted!");
                    return;
                }
                bookingSystem.removeBuilding(chosenBuilding);
                printNewMsg("Records and the corresponding references removed successfully!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }
        });
        removeRoomItem.addActionListener(e -> {
            try {
                printNewMsg("Which room would you like to remove from the system?");
                printCurrentRooms(bookingSystem.getRooms());
                printNewMsg("\n### WARNING ###\nOnce confirmed, all bookings made to this room and" +
                        "\nreference for corresponding building and person would also be removed,");
                printNewMsg("Enter your option, '0' to interrupt the action: ");
                int chosenRoom = Integer.parseInt(getInputString());
                if (chosenRoom == 0) {
                    printNewMsg("Action interrupted!");
                    return;
                }
                bookingSystem.removeRoom(chosenRoom);
                printNewMsg("Records and the corresponding references removed successfully!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }
        });
        removeBookingItem.addActionListener(e -> {
            try {
                printNewMsg("Which booking would you like to remove from the system?");
                printCurrentBookings(bookingSystem.getBookings());
                printNewMsg("\n### WARNING ###\nOnce confirmed, the person who made the booking and\n" +
                        "the room this booking reserved would lose this reference,\n" +
                        "and the records of this booking would be deleted from the system.");
                printNewMsg("Enter your option, '0' to interrupt the action: ");
                int chosenBooking = Integer.parseInt(getInputString());
                if (chosenBooking == 0) {
                    printNewMsg("Action interrupted!");
                    return;
                }

                bookingSystem.removeBooking(chosenBooking);
                printNewMsg("Records and the corresponding references removed successfully!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }
        });


        removeMenu.add(removePersonItem);
        removeMenu.add(removeBuildingItem);
        removeMenu.add(removeRoomItem);
        removeMenu.add(removeBookingItem);
    }


    private void addFunctionsToView(JMenu viewMenu) {
        allRoomAtATime = new JMenuItem("All rooms available at a given time");
        allRoomAtATimeBlock = new JMenuItem("All rooms available for a given time period");
        allBookingByAPerson = new JMenuItem("All booking made by a given person");
        allScheduleForARoom = new JMenuItem("The schedule including bookings and" +
                " free periods for a given room");
        allModels = new JMenuItem("All records of a type of entity registered to the system");


        allRoomAtATime.addActionListener(e -> {
            try{
                LocalDate underlyingDate = dateFormatCheck();
                if (underlyingDate == null) {
                    return;
                }
                boolean isToday = underlyingDate.equals(LocalDate.now());
                LocalTime underlyingTimePoint = timePointCheck(isToday);
                if (underlyingTimePoint == null) {
                    return;
                }
                timeClashCheck(underlyingDate, underlyingTimePoint, underlyingTimePoint);
                printNewMsg("\nEnd of this time point retrieving.\n");

            }
            catch (DateTimeParseException parseException) {
                popMsgToMainFrame("Input Date or Time parsing failed!", "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }

        });

        allRoomAtATimeBlock.addActionListener(e -> {
            try{
                LocalDate underlyingDate = dateFormatCheck();
                if (underlyingDate == null) {
                    return;
                }
                boolean isToday = underlyingDate.equals(LocalDate.now());
                LocalTime[] underlyingBlock = timeBlockCheck(isToday);
                if (underlyingBlock == null) {
                    return;
                }
                LocalTime validStartTime = underlyingBlock[0];
                LocalTime validEndTime = underlyingBlock[1];

                timeClashCheck(underlyingDate, validStartTime, validEndTime);
                printNewMsg("\nEnd of this time block retrieving.\n");
            }
            catch (DateTimeParseException parseException) {
                popMsgToMainFrame("Input Date or Time parsing failed!", "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }

        });

        allBookingByAPerson.addActionListener(e -> {
            try{
                printNewMsg("Which person's booking details would you like to check?");
                printCurrentPeople(bookingSystem.getPeople());
                printNewMsg("Enter your option, '0' to interrupt the action: ");
                int chosenPerson = Integer.parseInt(getInputString());
                if (chosenPerson == 0) {
                    popMsgToMainFrame("Action interrupted!", "Halt", 2);
                    return;
                }

                List<Booking> bookings = bookingSystem.getBookings();
                List<Person> people = bookingSystem.getPeople();
                Person person = people.get(chosenPerson - 1);
                for (Booking booking : bookings) {
                    if (person.equals(booking.getPerson())) {
                        printNewMsg(booking.getInfo());
                    }
                }
                printNewMsg("\nEnd of the retrieving request for booking.\n");
            }
            catch (DateTimeParseException parseException) {
                popMsgToMainFrame("Input Date or Time parsing failed!", "Error", 0);
            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }
        });

        allScheduleForARoom.addActionListener(e -> {
            try{
                printNewMsg("Which room's schedule would you like to check?");
                printCurrentRooms(bookingSystem.getRooms());
                printNewMsg("Enter your option, '0' to interrupt the action: ");
                int chosenRoom = Integer.parseInt(getInputString());
                if (chosenRoom == 0) {
                    printNewMsg("Action interrupted!");
                    return;
                }

                List<Room> rooms = bookingSystem.getRooms();
                List<Booking> bookings = bookingSystem.getBookings();
                Room room = rooms.get(chosenRoom - 1);

                printNewMsg("Here is the date today: " + LocalDate.now());
                printNewMsg("And the time for now: " + LocalTime.now());
                printNewMsg("The reservation information printed below (if any) " +
                        "indicates that the room is occupied at the time.\n" +
                        "Apart from that, from now on, the room is free .\n");
                for (Booking booking: bookings) {
                    if (room.equals(booking.getRoom())) {
                        printNewMsg(booking.getInfo());
                    }
                }

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }
        });

        allModels.addActionListener(e -> {
            try{
                printNewMsg(graphicalarmMenu);
                String keyboard = getInputString();
                int exceptedOption = Integer.parseInt(keyboard);
                switch (exceptedOption) {
                    case 1:
                        printCurrentPeople(bookingSystem.getPeople());
                        break;
                    case 2:
                        printCurrentBuildings(bookingSystem.getBuildings());
                        break;
                    case 3:
                        printCurrentRooms(bookingSystem.getRooms());
                        break;
                    case 4:
                        printCurrentBookings(bookingSystem.getBookings());
                        break;
                    default:
                        popMsgToMainFrame("The option entered is out of the range of displayed options!",
                                "Error", 0);
                }
            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popMsgToMainFrame("The option entered is out of the range of displayed options!",
                        "Error", 0);
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popMsgToMainFrame("Not a valid integer option!", "Error", 0);
            }
        });

        viewMenu.add(allRoomAtATime);
        viewMenu.add(allRoomAtATimeBlock);
        viewMenu.add(allBookingByAPerson);
        viewMenu.add(allScheduleForARoom);
        viewMenu.add(allModels);

    }

    private void setUpToolBar() {

        optionsButton = new JButton ("Options Info");
        logicButton = new JButton ("Logic Info");
        clearAll = new JButton("Clear All");
        // Implement the functionality for three buttons.
        implementHelpContent(true, optionsButton);
        implementHelpContent(false, logicButton);
        clearAll.addActionListener(e -> {
            stringBuffer.delete(0, stringBuffer.length());
            bookingSystem.notifyRefreshing();
        });

        toolBar.add(optionsButton);
        toolBar.add(logicButton);
        toolBar.add(clearAll);

        mainFrame.add(toolBar, BorderLayout.SOUTH);
        
    }

    private void setupComponents() {

        setUpMenu();
        setUpToolBar();
        
        mainFrame.setVisible(true);
        mainFrame.add(outputPane);
        mainFrame.setSize(FRAME_HEIGHT_AND_WIDTH, FRAME_HEIGHT_AND_WIDTH);

        // GUI will not close directly.
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Pop up a dialog box asking user whether to confirm the exit when the exit button was clicked.
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(mainFrame,
                        "Are you sure you want to close this GUI?", "Exit Program Message Box",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    popMsgToMainFrame(
                            "Thank you for using the GUI of the simple Booking System!", "Goodbye",1);
                    mainFrame.dispose(); // Close the GUI.
                }
            }
        });
    }



    private LocalDate dateFormatCheck() {
        LocalDate today = LocalDate.now();
        printNewMsg("\nEnter a date in this format: " + datePattern + "\n");
        String iDate = getInputString();
        LocalDate inputDate = LocalDate.parse(iDate);
        if (inputDate.isBefore(today)) {
            popMsgToMainFrame("Entered date is earlier than today!", "Error", 0);
            return null;
        }
        printNewMsg("Entered date validated!");
        return inputDate;
    }

    private LocalTime[] getTimeBlock(boolean today) {
        printNewMsg("Enter the start time in this format: " + timePattern + "\n");
        LocalTime rn = LocalTime.now();
        String iSTime = getInputString();
        LocalTime inputStartTime = LocalTime.parse(iSTime);

        if (today && inputStartTime.isBefore(rn)) {
            printNewMsg("Entered time is earlier than now!");
            return null;
        }

        printNewMsg("And enter the end time in the same format:");
        String iETime = getInputString();
        LocalTime inputEndTime = LocalTime.parse(iETime);


        // Legality not checked.
        LocalTime[] validStartEnd = {inputStartTime, inputEndTime};
        return validStartEnd;
    }

    private LocalTime[] bookingTimeFormatCheck(boolean today) {
        LocalTime[] originalSE = getTimeBlock(today);
        LocalTime inputStartTime = originalSE[0];
        LocalTime inputEndTime = originalSE[1];


        int differenceInHour = inputEndTime.getHour() - inputStartTime.getHour();
        int differenceInMin = inputEndTime.getMinute() - inputStartTime.getMinute();

        if (differenceInHour != 0) {
            differenceInMin += differenceInHour * 60;
        }

        if (differenceInMin < 5) {
            popMsgToMainFrame("Booking span is less than 5 min!", "Error", 0);
            return null;
        }

        if (differenceInMin % 5 != 0) {
            popMsgToMainFrame("Booking span not integral of five minutes!", "Error", 0);
            return null;
        }
        // Legality checked.
        printNewMsg("Validated entered start and end time!");
        return new LocalTime[]{inputStartTime, inputEndTime};
    }



    private boolean bookingConflictCheck(
            Room room, LocalDate underlyingDate,LocalTime[] underlyingStartAndEnd) {

        printNewMsg("Check whether the booking conflicted with another existed booking...");
        List<Booking> bookings = bookingSystem.getBookings();
        for (Booking booking : bookings) {
            if (booking.getDate().equals(underlyingDate)) {
                // Check if the underlying booking's endTime/startTime is:
                LocalTime underlyingStart = underlyingStartAndEnd[0];
                LocalTime underlyingEnd = underlyingStartAndEnd[1];
                // Check if interleaved
                boolean endAfterAnotherStart = underlyingEnd.isAfter(booking.getStartTime());
                boolean startBeforeAnotherEnd = underlyingStart.isBefore(booking.getEndTime());
                
                boolean sameRoom = room.equals(booking.getRoom());
                if (sameRoom &&
                        (endAfterAnotherStart && startBeforeAnotherEnd)) {
                    popMsgToMainFrame("Time entered or space chosen conflicted with existed booking unfortunately!", "Error", 0);
                    return true;
                }
            }
        }
        printNewMsg("No conflicts in time and space with existed bookings!");
        return false;
    }

    private LocalTime timePointCheck(boolean today) {
        printNewMsg("The time entered is expected ");
        printNewMsg("Enter the time point in this format: " +
                timePattern + "\n");
        LocalTime rn = LocalTime.now();
        String iTime = getInputString();
        LocalTime inputTime = LocalTime.parse(iTime);

        if (today && inputTime.isBefore(rn)) {
            popMsgToMainFrame("Entered time is earlier than now!", "Error", 0);
            return null;
        }
        return inputTime;
    }

    private LocalTime[] timeBlockCheck(boolean today) {
        LocalTime[] originalSE = getTimeBlock(today);
        LocalTime inputStartTime = originalSE[0];
        LocalTime inputEndTime = originalSE[1];

        int differenceInHour = inputEndTime.getHour() - inputStartTime.getHour();
        int differenceInMin = inputEndTime.getMinute() - inputStartTime.getMinute();

        if (differenceInHour != 0) {
            differenceInMin += differenceInHour * 60;
        }
        if (differenceInMin <= 0) {
            popMsgToMainFrame("Invalid time block!", "Error", 0);
            return null;
        }

        popMsgToMainFrame("Validated entered start and end time!", "Error", 0);
        return new LocalTime[]{inputStartTime, inputEndTime};
    }

    private void timeClashCheck(LocalDate underlyingDate, LocalTime requestedStart, LocalTime requestedEnd) {
        String buildingName;
        String roomName;
        List<Room> rooms = bookingSystem.getRooms();
        List<Booking> bookings = bookingSystem.getBookings();
        Room room;

        outer:
        for (int roomIndex = rooms.size() - 1; roomIndex >= 0; roomIndex--) {
            room = rooms.get(roomIndex);

            for (Booking booking: bookings) {
                if (booking.getRoom().equals(room)) {
                    boolean sameDate = booking.getDate().equals(underlyingDate);
                    boolean endAfterStart = requestedEnd.isAfter(booking.getStartTime());
                    boolean startBeforeEnd  = requestedStart.isBefore(booking.getEndTime());
                    if (sameDate && (endAfterStart || startBeforeEnd)) {
                        continue outer;
                    }
                }
            }
            buildingName = room.getBuilding().getName();
            roomName = room.getName();
            if (requestedStart.equals(requestedEnd)) {
                printNewMsg("\nAvailable room retrieved at time <" +
                                requestedStart + ">: \nBuilding: " + 
                        buildingName + ", Room: " + roomName + "\n,");
            }
            else {
                printNewMsg("\nAvailable room retrieved at period <" +
                        requestedStart + ">-<" + requestedEnd + ">: \nBuilding: " +
                        buildingName + ", Room: " + roomName + "\n");
            }
        }
    }
    

    private void printNewMsg(String stringToPrint) {
        stringBuffer.append(stringToPrint);
        bookingSystem.notifyRefreshing();
        stringBuffer.append("\n########## Dividing Line ##########\n");
    }

    private String getInputString() {
        String prompt = "Please follow the instructions on the screen to enter appropriate commands :";
        String query = "Query";
        String userInput = JOptionPane.showInputDialog(mainFrame, prompt, query, 3);
        if (userInput == null) {
            // 'Cancel' button clicked by the user.
            popMsgToMainFrame("Action interrupted!", "Halt", 2);
            throw new NullPointerException(); // Print nothing.
        }
        printNewMsg(userInput);
        return userInput;
    }

    private void popMsgToMainFrame(String msg, String title, int msgType) {
        JOptionPane.showMessageDialog(mainFrame, msg, title, msgType);
    }

    private void printCurrentPeople(List<Person> people) {
        String name;
        String email;
        printNewMsg("\nPrinting all current person's info...\n");
        for (int counter = 0; counter < people.size(); counter++) {
            name = people.get(counter).getName();
            email = people.get(counter).getEmail();
            printNewMsg(Integer.toString(counter + 1) + ". " +
                    name + " <" + email + ">");
        }
    }
    private void printCurrentBuildings(List<Building> buildings) {
        printNewMsg("\nPrinting all current building's info...\n");

        for (int counter = 0; counter < buildings.size(); counter++) {
            printNewMsg(Integer.toString(counter + 1) + ". " +
                    buildings.get(counter).getName());
        }
    }
    private void printCurrentRooms(List<Room> rooms) {
        String buildingName;
        String RoomName;
        printNewMsg("\nPrinting all current room's info...\n");
        for (int counter = 0; counter < rooms.size(); counter++) {
            buildingName = rooms.get(counter).getBuilding().getName();
            RoomName = rooms.get(counter).getName();

            printNewMsg(Integer.toString(counter + 1) + ". Room: " +
                    RoomName + "\n   Building: " + buildingName + "");
        }
    }
    private void printCurrentBookings(List<Booking> bookings) {
        printNewMsg("\nPrinting all current booking's info...\n");
        for (int counter = 0; counter < bookings.size(); counter++) {

            printNewMsg(Integer.toString(counter + 1) + ". " +
                    bookings.get(counter).getInfo());
        }
    }


    /**
     * This method is called when the model changes.(invoking notifier.firePropertyChange)
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == bookingSystem && evt.getPropertyName().equals("ChangeConfirmation")) {
            SwingUtilities.invokeLater(() -> outputField.setText(stringBuffer.toString()));
        }
    }
}
