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
                String name = getInputString("Please enter the name of the person");

                String email = getInputString("Please enter the email address of the person");
                boolean valid = bookingSystem.checkPersonInfoValidity(name, email);
                if (valid) {
                    bookingSystem.addPerson(name, email);
                    popValidationMsg("Registration of <" + name
                            + "> with a unique email address <" + email + "> is successful!");
                }
                else {
                    popErrorMsg("Repetitive email or invalid name detected, registration failed");
                }
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
        });

        addBuildingItem.addActionListener(e -> {
            try {
                String name = getInputString("Please enter the name of the building");
                String address = getInputString("Please enter the address of the building: ");

                boolean valid = bookingSystem.checkBuildingInfoValidity(name, address);
                if (valid) {
                    bookingSystem.addBuilding(name, address);
                    popValidationMsg("Registration of <"
                            + name + "> with a physical location <" + address + "> is successful!");
                }
                else {
                    popErrorMsg("Invalid name or address detected, registration failed");
                }
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
        });

        addRoomItem.addActionListener(e -> {
            try {

                printCurrentBuildings(false);

                int chosenBuilding = Integer.parseInt(getInputString(
                        "Which building would you like this room to be located in?\n"
                                + stringBuffer.toString()
                ));
                clearBuffer();


                String name = getInputString("Please enter the name of the Room");
                boolean valid = bookingSystem.checkRoomInfoValidity(name, chosenBuilding);
                if (!valid) {
                    popErrorMsg("Empty name detected or repetitive name detected" +
                                    " in the same building, room registration failed!");
                    return;
                }
                bookingSystem.addRoom(name, chosenBuilding);
                popValidationMsg("Registration for <" + name + "> with the building chosen is successful!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
        });

         addBookingItem.addActionListener(e -> {
            try {
                // Get Person and Room
                printCurrentPeople(false);
                int chosenPerson = Integer.parseInt(
                        getInputString(
                                "Who is requesting a reservation?\n"
                                        + "Enter your option, '0' to interrupt the action\n"
                                        + stringBuffer.toString())
                );
                clearBuffer();

                if (chosenPerson == 0) {
                    popInterruptionMsg();
                    return;
                }

                printCurrentRooms(false);
                int chosenRoom = Integer.parseInt(
                        getInputString("Which room would be booked?\n"
                                + "Enter your option, '0' to interrupt the action\n"
                                + stringBuffer.toString())
                );
                clearBuffer();

                if (chosenRoom == 0) {
                    popInterruptionMsg();
                    return;
                }

                // Date and Time legality check.
                LocalDate underlyingDate = getDateEntry(false);
                if (underlyingDate == null) {
                    popErrorMsg("Entered date is earlier than today!");
                    return;
                }


                LocalTime[] underlyingStartAndEnd = bookingTimeFormatCheck(underlyingDate);

                if (underlyingStartAndEnd == null) {
                    return;
                }
                List<Room> rooms = bookingSystem.getRooms();
                Room underlyingRoom = rooms.get(chosenRoom - 1);


                //Booking conflict check.
                if (bookingSystem.bookingConflictCheck(underlyingRoom, underlyingDate, underlyingStartAndEnd)) {
                    popErrorMsg("Time entered or space chosen conflicted with existed booking unfortunately!");
                    return;
                }

                bookingSystem.addBooking(underlyingDate, underlyingStartAndEnd, chosenPerson, chosenRoom);
                popValidationMsg("Booking registered successfully!");

            }
            catch (DateTimeParseException parseException) {
                popErrorMsg("Input Date or Time parsing failed!");
            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
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
                printCurrentPeople(false);
                int chosenPerson = Integer.parseInt(
                        getInputString("Which person would you like to remove from the system?"
                                + "\n     WARNING    \n"
                                + "Once confirmed, all bookings made by"
                                +"this person would be removed.\n"
                                + "Enter your option, '0' to interrupt the action\n"
                                + stringBuffer.toString())
                );
                clearBuffer();
                if (chosenPerson == 0) {
                    popInterruptionMsg();
                    return;
                }

                // Get the chosen person and delete its dependent instance/reference.
                bookingSystem.removePerson(chosenPerson);
                popValidationMsg("Records and the corresponding references removed successfully!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
            }
        });
        removeBuildingItem.addActionListener(e -> {
            try{

                printCurrentBookings(false);
                int chosenBuilding = Integer.parseInt(
                        getInputString("Which building would you like to remove from the system?"
                                +"\n     WARNING     \n"
                                + "Once confirmed, all rooms located in this building and all"
                                + "\nsubsequent booking records and their reference for person would also be removed!"
                                + "Enter your option, '0' to interrupt the action\n"
                                + stringBuffer.toString())
                );
                clearBuffer();

                if (chosenBuilding == 0) {
                    popInterruptionMsg();
                    return;
                }
                bookingSystem.removeBuilding(chosenBuilding);
                popValidationMsg("Records and the corresponding references removed successfully!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
            }
        });
        removeRoomItem.addActionListener(e -> {
            try {

                printCurrentRooms(false);
                int chosenRoom = Integer.parseInt(
                        getInputString("Which room would you like to remove from the system?"
                                + "\n     WARNING     \n"
                                + "Once confirmed, all bookings made to this room and"
                                + "\nreference for corresponding building and person would also be removed."
                                + "Enter your option, '0' to interrupt the action\n"
                                + stringBuffer.toString())
                );
                clearBuffer();

                if (chosenRoom == 0) {
                    popInterruptionMsg();
                    return;
                }
                bookingSystem.removeRoom(chosenRoom);
                popValidationMsg("Records and the corresponding references removed successfully!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
            }
        });
        removeBookingItem.addActionListener(e -> {
            try {

                printCurrentBookings(false);
                int chosenBooking = Integer.parseInt(
                        getInputString("Which booking would you like to remove from the system?"
                                + "\n     WARNING     \n"
                                + "Once confirmed, the person who made the booking and\n"
                                + "the room this booking reserved would lose this reference,\n"
                                + "and the records of this booking would be deleted from the system."
                                + "Enter your option, '0' to interrupt the action\n"
                                + stringBuffer.toString())
                );
                if (chosenBooking == 0) {
                    popInterruptionMsg();
                    return;
                }

                bookingSystem.removeBooking(chosenBooking);
                popValidationMsg("Records and the corresponding references removed successfully!");

            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
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
                LocalDate underlyingDate = getDateEntry(true);
                if (underlyingDate == null) {
                    return;
                }

                LocalTime underlyingTimePoint = searchTimePointCheck();
                if (underlyingTimePoint == null) {
                    return;
                }
                popValidationMsg("The time entered is expected");
                timeClashCheck(underlyingDate, underlyingTimePoint, underlyingTimePoint);

            }
            catch (DateTimeParseException parseException) {
                popErrorMsg("Input Date or Time parsing failed!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
        });

        allRoomAtATimeBlock.addActionListener(e -> {
            try{
                LocalDate underlyingDate = getDateEntry(true);
                if (underlyingDate == null) {
                    return;
                }
                LocalTime[] underlyingBlock = searchTimeBlockCheck(underlyingDate);
                if (underlyingBlock == null) {
                    return;
                }
                LocalTime validStartTime = underlyingBlock[0];
                LocalTime validEndTime = underlyingBlock[1];

                timeClashCheck(underlyingDate, validStartTime, validEndTime);
            }
            catch (DateTimeParseException parseException) {
                popErrorMsg("Input Date or Time parsing failed!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }

        });

        allBookingByAPerson.addActionListener(e -> {
            try{

                printCurrentPeople(false);
                int chosenPerson = Integer.parseInt(
                        getInputString("Which person's booking details would you like to check?\n"
                                + "Enter your option, '0' to interrupt the action\n")
                );
                clearBuffer();

                if (chosenPerson == 0) {
                    popInterruptionMsg();
                    return;
                }

                List<Booking> bookings = bookingSystem.getBookings();
                List<Person> people = bookingSystem.getPeople();
                Person person = people.get(chosenPerson - 1);
                for (Booking booking : bookings) {
                    if (person.equals(booking.getPerson())) {
                        stringBuffer.append(booking.getInfo());

                    }
                }
                stringBuffer.append("\nEnd of the retrieving request for booking.\n");
                printBuffer("List", -1);

            }
            catch (DateTimeParseException parseException) {
                popErrorMsg("Input Date or Time parsing failed!");
            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
            }
        });

        allScheduleForARoom.addActionListener(e -> {
            try{

                printCurrentRooms(false);
                int chosenRoom = Integer.parseInt(
                        getInputString("Which room's schedule would you like to check?\n"
                                + "Enter your option, '0' to interrupt the action\n"
                                + stringBuffer.toString())
                );
                clearBuffer();

                if (chosenRoom == 0) {
                    popInterruptionMsg();
                    return;
                }

                List<Room> rooms = bookingSystem.getRooms();
                List<Booking> bookings = bookingSystem.getBookings();
                Room room = rooms.get(chosenRoom - 1);

                popPromptMsg("Here is the date today: " + LocalDate.now() + "\n"
                        +"And the time for now: " + LocalTime.now() + "\n"
                        +"The reservation information printed below (if any) "
                        +"indicates that the room is occupied at the time.\n"
                        +"Apart from that, from now on, the room is free .\n");
                for (Booking booking: bookings) {
                    if (room.equals(booking.getRoom())) {
                        stringBuffer.append(booking.getInfo());
                    }
                }
                stringBuffer.append("\nEnd of the List.");
                printBuffer("Retrieval", -1);


            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
            }
        });

        allModels.addActionListener(e -> {
            try{
//                printNewMsg(graphicalarmMenu);
                String keyboard = getInputString(graphicalarmMenu);
                int exceptedOption = Integer.parseInt(keyboard);
                switch (exceptedOption) {
                    case 1:
                        printCurrentPeople(true);
                        break;
                    case 2:
                        printCurrentBuildings(true);
                        break;
                    case 3:
                        printCurrentRooms(true);
                        break;
                    case 4:
                        printCurrentBookings(true);
                        break;
                    default:
                        popErrorMsg("The option entered is out of the range of displayed options!");
                }
            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                popErrorMsg("The option entered is out of the range of displayed options!");
            }
            catch (NullPointerException nullPointerException){
                // Do nothing as user click the cancel.
            }
            catch (NumberFormatException nfe) {
                popErrorMsg("Not a valid integer option!");
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

    private void popValidationMsg(String prompt) {
        popMsgToMainFrame(prompt, "Confirmation", -1);
    }

    private void popPromptMsg(String prompt) {
        popMsgToMainFrame(prompt, "Notice", -1);
    }

    private void popErrorMsg(String msg) {
        popMsgToMainFrame(msg, "Error", 0);
    }

    private void popInterruptionMsg() {
        popMsgToMainFrame("Action interrupted!", "Halt", 2);
    }

    private void printBuffer(String title, int msgType) {
        popMsgToMainFrame(stringBuffer.toString(), title, msgType);
        stringBuffer.delete(0, stringBuffer.length());
    }

    private void clearBuffer() {
        stringBuffer.delete(0, stringBuffer.length());
    }

    // Check whether the entered date is earlier than today for new booking case
    private LocalDate dateCheck(String iDate, boolean calledByView) {
        LocalDate inputDate = LocalDate.parse(iDate);
        if (bookingSystem.earlyDateCheck(inputDate, calledByView)) {
            return null;
        }
        popValidationMsg("Entered date validated!");
        return inputDate;
    }

    private LocalDate getDateEntry(boolean calledByView) {
        LocalDate underlyingDate = null;
        popMsgToMainFrame("Please follow the instructions to enter the date and time.\n"
                        + "The entered DATE and TIME must be no earlier than now(when sending the request).",
                "Prompt", 2);
        String iDate = getInputString("Enter a date in this format: " + datePattern);

        // Handle the case of empty entry.
        try{
            underlyingDate = dateCheck(iDate, calledByView);
        }
        catch (DateTimeParseException dtpException) {
            popErrorMsg("Input Date or Time parsing failed!");
        }
        return underlyingDate;
    }


    private LocalTime[] getTimeBlock(LocalDate dateEntry, boolean calledByView) {
        LocalTime[] validStartAndEnd = null;
        try {

            String iSTime = getInputString("Enter the start time in this format: " + timePattern);
            LocalTime inputStartTime = LocalTime.parse(iSTime);
            boolean today = bookingSystem.earlyDateCheck(dateEntry, calledByView);
            boolean earlyTime = bookingSystem.earlyTimeCheck(inputStartTime, calledByView);

            // Check whether the time block called by the addBooking methods
            // is later than the point when the request is made.
            if (today && earlyTime && !calledByView) {
                return null;
            }

            String iETime = getInputString("And enter the end time in the same format: " + timePattern);
            LocalTime inputEndTime = LocalTime.parse(iETime);

            // Legality not checked.
            validStartAndEnd = new LocalTime[]{inputStartTime, inputEndTime};
        }
        // Handle the case of empty entry.
        catch (DateTimeParseException dtpException) {
            popErrorMsg("Input Date or Time parsing failed!");
        }

        return validStartAndEnd;
    }


    private LocalTime[] bookingTimeFormatCheck(LocalDate underlyingDate) {

        LocalTime[] originalSE = getTimeBlock(underlyingDate, false);
        LocalTime inputStartTime = null;
        LocalTime inputEndTime = null;
        try {
            inputStartTime = originalSE[0];
            inputEndTime = originalSE[1];
        }
        catch (NullPointerException npe) {
            popErrorMsg("Entered time is earlier than now!");
        }

        int differenceInHour = inputEndTime.getHour() - inputStartTime.getHour();
        int differenceInMin = inputEndTime.getMinute() - inputStartTime.getMinute();

        if (differenceInHour != 0) {
            differenceInMin += differenceInHour * 60;
        }

        if (differenceInMin < 5) {
            popErrorMsg("Booking span is less than 5 min!");
            return null;
        }

        if (differenceInMin % 5 != 0) {
            popErrorMsg("Booking span not integral of five minutes!");
            return null;
        }
        // Legality checked.
        popValidationMsg("The entered start and end time conforms to the format!");
        return new LocalTime[]{inputStartTime, inputEndTime};
    }


    private LocalTime searchTimePointCheck() {
        String iTime = getInputString("Enter the time point in this format: "
                + timePattern);
        LocalTime inputTime = LocalTime.parse(iTime);

        return inputTime;
    }

    private LocalTime[] searchTimeBlockCheck(LocalDate entryDate) {
        LocalTime[] originalSE = getTimeBlock(entryDate, true);
        // Deeper level of exception would have been caught in the method getTimeBlock.
        assert originalSE != null;
        LocalTime inputStartTime = originalSE[0];
        LocalTime inputEndTime = originalSE[1];

        int differenceInHour = inputEndTime.getHour() - inputStartTime.getHour();
        int differenceInMin = inputEndTime.getMinute() - inputStartTime.getMinute();

        if (differenceInHour != 0) {
            differenceInMin += differenceInHour * 60;
        }
        if (differenceInMin <= 0) {
            popErrorMsg("Invalid time block!");
            return null;
        }

        popMsgToMainFrame("Entered start and end time pass the validation!", "Message", 1);
        return new LocalTime[]{inputStartTime, inputEndTime};
    }

    private void timeClashCheck(LocalDate underlyingDate, LocalTime requestedStart, LocalTime requestedEnd) {
        String buildingName;
        String roomName;
        List<Room> rooms = bookingSystem.getRooms();
        List<Booking> bookings = bookingSystem.getBookings();
        Room room;
        LocalTime outputStart, outputEnd;
        boolean isClashed;

        outer:
        for (int roomIndex = rooms.size() - 1; roomIndex >= 0; roomIndex--) {
            room = rooms.get(roomIndex);
            isClashed = false;
            outputStart = requestedStart;
            outputEnd = requestedEnd;

            for (Booking booking: bookings) {
                if (booking.getRoom().equals(room)) {
                    boolean sameDate = booking.getDate().equals(underlyingDate);
                    boolean reqEndAfterEqStart
                            = (requestedEnd.isAfter(booking.getStartTime())
                            ||requestedEnd.equals(booking.getStartTime()));

                    boolean reqStartBeforeEqEnd
                            = (requestedStart.isBefore(booking.getEndTime())
                            || requestedStart.equals(booking.getEndTime()));

                    boolean reqStartAfterEqStart
                            = (requestedStart.isAfter(booking.getStartTime())
                            || requestedStart.equals(booking.getStartTime()));

                    boolean reqEndBeforeEqEnd
                            = (requestedEnd.isBefore(booking.getEndTime())
                            || requestedEnd.equals(booking.getEndTime()));

                    // Given the constraint on the outer scope, only
                    // those valid start-end pair(start before end) would be considered
                    if (sameDate) {
                        if (reqStartAfterEqStart && reqEndBeforeEqEnd) {
                            isClashed = true;
                        }
                        else if (reqEndAfterEqStart && reqEndBeforeEqEnd) {
                            outputEnd = booking.getStartTime();
                        }
                        else if (reqStartAfterEqStart && reqStartBeforeEqEnd) {
                            outputStart = booking.getEndTime();
                        }
                    }
                }
            }
            buildingName = room.getBuilding().getName();
            roomName = room.getName();
            if (!isClashed) {
                if (requestedStart.equals(requestedEnd)) {
                    stringBuffer.append("\nAvailable room retrieved at time <" +
                            outputStart + ">: \nBuilding: " +
                            buildingName + ", Room: " + roomName + "\n,");
                }
                else {
                    stringBuffer.append("\nAvailable room retrieved at period <" +
                            outputStart + ">-<" + outputEnd + ">: \nBuilding: " +
                            buildingName + ", Room: " + roomName + "\n");
                }
            }
        }
        stringBuffer.append("\nEnd of the List.");
        printBuffer("Retrieval", -1);
    }


    private void printNewMsg(String stringToPrint) {
        stringBuffer.append(stringToPrint);
        bookingSystem.notifyRefreshing();
//        stringBuffer.append("\n####################\n");
        clearBuffer();
    }

    private String getInputString(String prompt) {

        String userInput = JOptionPane.showInputDialog(mainFrame, prompt, "Query", 3);
        if (userInput == null) {
            // 'Cancel' button clicked by the user.
            popInterruptionMsg();
            throw new NullPointerException(); // Print nothing.
        }
        printNewMsg(userInput);
        return userInput;
    }

    private void popMsgToMainFrame(String msg, String title, int msgType) {
        JOptionPane.showMessageDialog(mainFrame, msg, title, msgType);
    }

    /**
     * Append all registered people's information into the stringBuffer,
     * and print it if the method is called by viewing functions.
     * @param calledByView Whether invoked by the viewing function.
     */
    private void printCurrentPeople(boolean calledByView) {
        String name;
        String email;
        List<Person> people = bookingSystem.getPeople();
        for (int counter = 0; counter < people.size(); counter++) {
            name = people.get(counter).getName();
            email = people.get(counter).getEmail();
            stringBuffer.append(Integer.toString(counter + 1) + ". " +
                    people.get(counter).getInfo() + "\n");
        }
        if (calledByView) {
            printBuffer("Person List", -1);
        }
    }

    /**
     * Append all registered buildings' information to the stringBuffer,
     * and print it if the method is called by viewing functions.
     * @param calledByView Whether invoked by the viewing function.
     */
    private void printCurrentBuildings(boolean calledByView) {

        List<Building> buildings = bookingSystem.getBuildings();
        for (int counter = 0; counter < buildings.size(); counter++) {
            stringBuffer.append(Integer.toString(counter + 1) + ". " +
                    buildings.get(counter).getInfo() + "\n");
        }
        if (calledByView) {
            printBuffer("Building List", -1);
        }
    }

    /**
     * Append all registered rooms' information to the stringBuffer,
     * and print it if the method is called by viewing functions.
     * @param calledByView Whether invoked by the viewing function.
     */
    private void printCurrentRooms(boolean calledByView) {

        List<Room> rooms = bookingSystem.getRooms();
        for (int counter = 0; counter < rooms.size(); counter++) {

            stringBuffer.append(Integer.toString(counter + 1) + ". " +
                    rooms.get(counter).getInfo() + "\n");
        }
        if (calledByView) {
            printBuffer("Room List", -1);
        }
    }

    /**
     * Append all registered bookings' information to the stringBuffer,
     * and print it if the method is called by viewing functions.
     * @param calledByView Whether invoked by the viewing function.
     */
    private void printCurrentBookings(boolean calledByView) {
        List<Booking> bookings = bookingSystem.getBookings();
        for (int counter = 0; counter < bookings.size(); counter++) {

            stringBuffer.append(Integer.toString(counter + 1) + ". " +
                    bookings.get(counter).getInfo() + "\n");
        }
        if (calledByView) {
            printBuffer("Booking List", -1);
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
