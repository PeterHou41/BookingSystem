package src.delegateView;

import src.model.Booking;
import src.model.BookingSystem;
import src.model.Building;
import src.model.Person;
import src.model.Room;

import java.io.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import java.util.List;

import static src.delegateView.SharedConstants.*;


/** Represents a text-based UI. */
public class Terminal {

    private BookingSystem bookingSystem;

    private boolean exitState;

    private final String mainMenu =
            "\nEnter an option number (without period) to interact with the system\n" +
            "1. Add\n" +
            "2. Remove\n" +
            "3. View\n" +
            "4. Load\n" +
            "5. Save\n" +
            "6. Help\n" +
            "7. Exit\n" +
            "Enter your option: ";

    private final String viewMenu =
            "Which option of event would you like to view?\n" +
            "1. All rooms available at a given time \n" +
            "2. All rooms available for a given time period\n" +
            "3. All booking made by a given person\n" +
            "4. The schedule including bookings and free periods for a given room\n" +
            "5. All records of a type of entity registered to the system\n" +
            "6. Back to the previous level of menu\n" +
            "Enter your option: ";


    private final String defaultLogPrefix = "./src/logs/";
    private final String serialisedSuffix = ".ser";

    private BufferedReader br;
    private PrintWriter pw;

    /**
     * Instantiate a text-based UI.
     * @param sharedBookingSystem Model of the booking system shared by GUI and TUI.
     */
    public Terminal(BookingSystem sharedBookingSystem) {

        bookingSystem = sharedBookingSystem;
        exitState = false;
        InputStream is = System.in;
        br = new BufferedReader(new InputStreamReader(is));
        pw = new PrintWriter(System.out, true);
        String keyboard;
        int expectedOption;

        pw.println("Welcome to the text-based User Interface of the simple Room Booking System.");

        while (!exitState) {
            try {
                pw.println(mainMenu);
                keyboard = br.readLine();
                expectedOption = Integer.parseInt(keyboard);
                switch (expectedOption) {
                    case 1:
                        add();
                        break;
                    case 2:
                        remove();
                        break;
                    case 3:
                        view();
                        break;
                    case 4:
                        load();
                        break;
                    case 5:
                        save();
                        break;
                    case 6:
                        printMainHelp();
                        break;
                    case 7:
                        pw.println("Thank you for your using.");
                        exitState = true;
                }
            }
            catch (ClassNotFoundException cnfe) {
                pw.println("Class Not found and the file is failed to load/save!");
            }
            catch (DateTimeParseException parseException) {
                pw.println("Input Date or Time parsing failed!");
                pw.println("Re-listening requests...");
            }
            catch (FileNotFoundException fileNotFoundException) {
                pw.println("The file path you entered does not exist in this level of directory!");
            }
            catch (IndexOutOfBoundsException outOfBoundsException) {
                pw.println("The option entered is out of the range of displayed options!");
            }
            catch (IOException e) {
                pw.println("Oops...");
            }
            catch (NumberFormatException nfe) {
                pw.println("Not a valid integer option!");
                pw.println("Re-listening requests...");
            }
        }
    }

    private void add() {
        printarmMenu();
        try{
            String keyboard = br.readLine();
            int exceptedOption = Integer.parseInt(keyboard);
            switch (exceptedOption) {
                case 1:
                    addPersonCheck();
                    break;
                case 2:
                    addBuildingCheck();
                    break;
                case 3:
                    addRoomCheck();
                    break;
                case 4:
                    addBookingCheck();
                    break;
                default:
                    pw.println("Back to previous level...");
                    break;
            }
        }
        catch (IOException e) {
            pw.println("Oops...");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
            pw.println("Back to previous level...");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }

    private void remove() {
        printarmMenu();
        try{
            String keyboard = br.readLine();
            int exceptedOption = Integer.parseInt(keyboard);
            switch (exceptedOption) {
                case 1:
                    removePerson();
                    break;
                case 2:
                    removeBuilding();
                    break;
                case 3:
                    removeRoom();
                    break;
                case 4:
                    removeBooking();
                    break;
                default:
                    pw.println("Back to previous level...");
                    break;
            }
        }
        catch (IOException e) {
            pw.println("Oops...");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
            pw.println("Back to previous level...");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }

    }

    private void view() {
        pw.println(viewMenu);
        try{
            String keyboard = br.readLine();
            int exceptedOption = Integer.parseInt(keyboard);
            switch (exceptedOption) {
                case 1:
                    allRoomsGivenTime();
                    break;
                case 2:
                    allRoomsGivenTimeBlock();
                    break;
                case 3:
                    allBookingGivenPerson();
                    break;
                case 4:
                    allScheduleGivenRoom();
                    break;
                case 5:
                    models();
                    break;
                case 6:
                    pw.println("Back to previous level...");
                    break;
            }
        }
        catch (IOException e) {
            pw.println("Oops...");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
            pw.println("Back to previous level...");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }

    private void models() {
        printarmMenu();
        try{
            String keyboard = br.readLine();
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
                case 5:
                    pw.println("Back to previous level...");
                    break;
            }
        }
        catch (IOException e) {
            pw.println("Oops...");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }

    }


    private void load() throws IOException, ClassNotFoundException {
        printLogs();
        pw.println("Please enter the name of the file you want to load (without prefix/folder name and suffix): ");

        String expectedPath = br.readLine();
        if (expectedPath.contains(" ") || expectedPath.equals("")) {
            pw.println("File name left empty or contains space!");
            return;
        }
        String actualPath = defaultLogPrefix + expectedPath + serialisedSuffix;

        FileInputStream fis = new FileInputStream(actualPath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        BookingSystem assumingSystem = (BookingSystem) ois.readObject();
        bookingSystem.updatingSystem(assumingSystem);
        fis.close();
        ois.close();
        pw.printf("\nSystem state loaded from the file <%s> successfully!\n", expectedPath + serialisedSuffix);
        bookingSystem.notifyRefreshing();
    }
    private void save() throws IOException {
        printLogs();
        pw.println("###### Notice ######\n" +
                "Entering a file name that duplicates an existing file name will\n" +
                "cause the existing file to be overwritten.\n######  EON  ######");
        pw.println("Now please enter the name of the file(without prefix/folder name and suffix): ");

        String expectedPath = br.readLine();
        if (expectedPath.contains(" ") || expectedPath.equals("")) {
            pw.println("File name left empty or contains space!");
            return;
        }
        String actualPath = defaultLogPrefix + expectedPath + serialisedSuffix;

        FileOutputStream fos = new FileOutputStream(actualPath);

        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(bookingSystem);
        oos.close();
        fos.close();
        pw.printf("\nSystem state saved to the file <%s> successfully!\n", expectedPath + serialisedSuffix);
        bookingSystem.notifyRefreshing();
    }

    private void printLogs() throws IOException {
        pw.println("The saved files will be stored in the subfolder /logs of this system program's parent directory.");
        pw.println("Listing the files where the system status stored in the folder ../logs");

        File directoryPath = new File(defaultLogPrefix);
        File[] allFiles = directoryPath.listFiles();
        for (File file: allFiles) {
            if (allFiles[0] != null) {
                pw.println("File name: " + defaultLogPrefix + file.getName());
            }

        }
    }


    private void addPersonCheck() {
        try {
            pw.println("Please enter the name of the person: ");
            String inputName = br.readLine();
            pw.println("Please enter the email address of the person: ");
            String inputEmail = br.readLine();
            boolean valid = bookingSystem.checkPersonInfoValidity(inputName, inputEmail);
            if (valid) {
                bookingSystem.addPerson(inputName, inputEmail);
                pw.printf("Registration of <%s> with a unique email address <%s> is successful!\n", inputName, inputEmail);
            }
            else {
                pw.println("Repetitive email or invalid name detected, registration failed.");
            }
        }
        catch (IOException e) {
            pw.println("An IOException thrown for unknown reason.");
        }
    }

    private void addBuildingCheck() {
        try {
            pw.println("Please enter the name of the building: ");
            String inputName = br.readLine();
            pw.println("Please enter the address of the building: ");
            String inputAddress = br.readLine();

            boolean valid = bookingSystem.checkBuildingInfoValidity(inputName, inputAddress);

            if (valid) {
                bookingSystem.addBuilding(inputName, inputAddress);
                pw.printf("Registration of <%s> with a physical location <%s> is successful!\n", inputName, inputAddress);
            }
            else {
                pw.println("Invalid name or address detected, registration failed.");
            }
        }
        catch (IOException e) {
            pw.println("An IOException thrown for unknown reason.");
        }
    }

    private void addRoomCheck() {
        try {
            // Choose building.
            pw.println("Which building you would like this room located in?");
            printCurrentBuildings(bookingSystem.getBuildings());
            pw.println("Enter your option, '0' to step back: ");
            int chosenBuilding = Integer.parseInt(br.readLine());
            if (chosenBuilding == 0) {
                pw.println("Action interrupted!");
                return;
            }

            pw.println("Please enter the name of the Room: ");
            String inputName = br.readLine();
            boolean valid = bookingSystem.checkRoomInfoValidity(inputName, chosenBuilding);
            if (!valid) {
                pw.println("Empty name detected or repetitive name detected in the same building, room registration failed!");
                return;
            }
            bookingSystem.addRoom(inputName, chosenBuilding);


            pw.printf("Registration for <%s> with the building chosen is successful!\n",
                    inputName);

        }
        catch (IOException ioe) {
            pw.println("An IOException thrown for unknown reason.");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }

    private void addBookingCheck() {
        try {
            // Get Person and Room
            pw.println("Which person is requesting a booking?");
            printCurrentPeople(bookingSystem.getPeople());
            pw.println("Enter your option, '0' to interrupt the action: ");
            int chosenPerson = Integer.parseInt(br.readLine());
            if (chosenPerson == 0) {
                pw.println("Action interrupted!");
                return;
            }

            pw.println("Which room would be booked?");
            printCurrentRooms(bookingSystem.getRooms());
            pw.println("Enter your option, '0' to interrupt the action: ");
            int chosenRoom = Integer.parseInt(br.readLine());
            if (chosenRoom == 0) {
                pw.println("Action interrupted!");
                return;
            }


            // Date and Time legality check.
            pw.println("The entered date and time must be no earlier than now(when sending the request).");
            pw.println("Please enter the date for the booking.");
            LocalDate underlyingDate = dateFormatCheck();
            if (underlyingDate == null) {
                return;
            }
            boolean isToday = underlyingDate.equals(LocalDate.now());
            pw.println("Please enter the start and the end time for the booking.");
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
            pw.println("Booking registered successfully!");

        }
        catch (IOException ioe) {
            pw.println("An IOException thrown for unknown reason.");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }

    private void removePerson() {
        try {
            pw.println("Which person would you like to remove from the system?");
            printCurrentPeople(bookingSystem.getPeople());
            pw.println("\n### WARNING ###\nOnce confirmed, all bookings made by" +
                    "\nthis person would be removed,");
            pw.println("Enter your option, '0' to interrupt the action: ");

            int chosenPerson = Integer.parseInt(br.readLine());
            if (chosenPerson == 0) {
                pw.println("Action interrupted!");
                return;
            }

            // Get the chosen person and delete its dependent instance/reference.
            bookingSystem.removePerson(chosenPerson);
            pw.println("Records and the corresponding references removed successfully!");

        }
        catch (IOException ioe) {
            pw.println("An IOException thrown for unknown reason.");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
            pw.println("Back to previous level...");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }



    private void removeBuilding() {
        try {
            pw.println("Which building would you like to remove from the system?");
            printCurrentBuildings(bookingSystem.getBuildings());
            pw.println("\n### WARNING ###\nOnce confirmed, all rooms located in this building and all " +
                    "\nsubsequent booking records and their reference for person would also be removed!");
            pw.println("Enter your option, '0' to interrupt the action: ");

            int chosenBuilding = Integer.parseInt(br.readLine());
            if (chosenBuilding == 0) {
                pw.println("Action interrupted!");
                return;
            }
            bookingSystem.removeBuilding(chosenBuilding);
            pw.println("Records and the corresponding references removed successfully!");
        }
        catch (IOException ioe) {
            pw.println("An IOException thrown for unknown reason.");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
            pw.println("Back to previous level...");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }

    private void removeRoom() {
        try {
            pw.println("Which room would you like to remove from the system?");
            printCurrentRooms(bookingSystem.getRooms());
            pw.println("\n### WARNING ###\nOnce confirmed, all bookings made to this room and" +
                    "\nreference for corresponding building and person would also be removed,");
            pw.println("Enter your option, '0' to interrupt the action: ");
            int chosenRoom = Integer.parseInt(br.readLine());
            if (chosenRoom == 0) {
                pw.println("Action interrupted!");
                return;
            }
            bookingSystem.removeRoom(chosenRoom);
            pw.println("Records and the corresponding references removed successfully!");

        }
        catch (IOException ioe) {
            pw.println("An IOException thrown for unknown reason.");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
            pw.println("Back to previous level...");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }

    }

    private void removeBooking() {
        try {
            pw.println("Which booking would you like to remove from the system?");
            printCurrentBookings(bookingSystem.getBookings());
            pw.println("\n### WARNING ###\nOnce confirmed, the person who made the booking and\n" +
                    "the room this booking reserved would lose this reference,\n" +
                    "and the records of this booking would be deleted from the system.");
            pw.println("Enter your option, '0' to interrupt the action: ");
            int chosenBooking = Integer.parseInt(br.readLine());
            if (chosenBooking == 0) {
                pw.println("Action interrupted!");
                return;
            }

            bookingSystem.removeBooking(chosenBooking);
            pw.println("Records and the corresponding references removed successfully!");
        }
        catch (IOException ioe) {
            pw.println("An IOException thrown for unknown reason.");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
            pw.println("Back to previous level...");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }



    private LocalDate dateFormatCheck() throws IOException {
        LocalDate today = LocalDate.now();
        pw.printf("\nEnter a date in this format: %s\n", datePattern);
        String iDate = br.readLine();
        LocalDate inputDate = LocalDate.parse(iDate);
        if (inputDate.isBefore(today)) {
            pw.println("Entered date is earlier than today!");
            return null;
        }
        pw.println("Entered date validated!");
        return inputDate;
    }

    private LocalTime[] getTimeBlock(boolean today) throws IOException{
        pw.printf("Enter the start time in this format: %s\n", timePattern);
        LocalTime rn = LocalTime.now();
        String iSTime = br.readLine();
        LocalTime inputStartTime = LocalTime.parse(iSTime);

        if (today && inputStartTime.isBefore(rn)) {
            pw.println("Entered time is earlier than now!");
            return null;
        }

        pw.println("And enter the end time in the same format:");
        String iETime = br.readLine();
        LocalTime inputEndTime = LocalTime.parse(iETime);


        // Legality not checked.
        LocalTime[] validStartEnd = {inputStartTime, inputEndTime};
        return validStartEnd;
    }

    private LocalTime[] bookingTimeFormatCheck(boolean today) throws IOException{
        LocalTime[] originalSE = getTimeBlock(today);
        LocalTime inputStartTime = originalSE[0];
        LocalTime inputEndTime = originalSE[1];


        int differenceInHour = inputEndTime.getHour() - inputStartTime.getHour();
        int differenceInMin = inputEndTime.getMinute() - inputStartTime.getMinute();

        if (differenceInHour != 0) {
            differenceInMin += differenceInHour * 60;
        }

        if (differenceInMin < 5) {
            pw.println("Booking span is less than 5 min!");
            return null;
        }

        if (differenceInMin % 5 != 0) {
            pw.println("Booking span not integral of five minutes!");
            return null;
        }
        // Legality checked.
        pw.println("Validated entered start and end time!");
        return new LocalTime[]{inputStartTime, inputEndTime};
    }



    private boolean bookingConflictCheck(
            Room room, LocalDate underlyingDate,LocalTime[] underlyingStartAndEnd) {

        pw.println("Check whether the booking conflicted with another existed booking...");
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
                    pw.println("Time entered or space chosen conflicted with existed booking unfortunately!");
                    return true;
                }
            }
        }
        pw.println("No conflicts in time and space with existed bookings!");
        return false;
    }



    private void allRoomsGivenTime() {
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
            pw.println("\nEnd of this time point retrieving.\n");
        }
        catch (IOException ioe) {
            pw.println("Unknown IOException thrown!");
        }
    }

    private void allRoomsGivenTimeBlock() {
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
            pw.println("\nEnd of this time block retrieving.\n");
        }
        catch (IOException ioe) {
            pw.println("Unknown IOException thrown!");
        }
    }

    private LocalTime timePointCheck(boolean today) throws IOException {
        pw.println("The time entered is expected ");
        pw.printf("Enter the time point in this format: %s\n", timePattern);
        LocalTime rn = LocalTime.now();
        String iTime = br.readLine();
        LocalTime inputTime = LocalTime.parse(iTime);

        if (today && inputTime.isBefore(rn)) {
            pw.println("Entered time is earlier than now!");
            return null;
        }
        return inputTime;
    }

    private LocalTime[] timeBlockCheck(boolean today) throws IOException {
        LocalTime[] originalSE = getTimeBlock(today);
        LocalTime inputStartTime = originalSE[0];
        LocalTime inputEndTime = originalSE[1];

        int differenceInHour = inputEndTime.getHour() - inputStartTime.getHour();
        int differenceInMin = inputEndTime.getMinute() - inputStartTime.getMinute();

        if (differenceInHour != 0) {
            differenceInMin += differenceInHour * 60;
        }
        if (differenceInMin <= 0) {
            pw.println("Invalid time block!");
            return null;
        }

        pw.println("Validated entered start and end time!");
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
                pw.printf("\nAvailable room retrieved at time <%s>: \nBuilding: %s, Room: %s\n",
                        requestedStart, buildingName, roomName);
            }
            else {
                pw.printf("\nAvailable room retrieved at period <%s>-<%s>: \nBuilding: %s, Room: %s\n",
                        requestedStart, requestedEnd, buildingName, roomName);
            }

        }
    }

    private void allBookingGivenPerson() {
        try {
            pw.println("Which person's booking details would you like to check?");
            printCurrentPeople(bookingSystem.getPeople());
            pw.println("Enter your option, '0' to interrupt the action: ");
            int chosenPerson = Integer.parseInt(br.readLine());
            if (chosenPerson == 0) {
                pw.println("Action interrupted!");
                return;
            }

            List<Booking> bookings = bookingSystem.getBookings();
            List<Person> people = bookingSystem.getPeople();
            Person person = people.get(chosenPerson - 1);
            for (Booking booking : bookings) {
                if (person.equals(booking.getPerson())) {
                    pw.print(booking.getInfo());
                }
            }
            pw.println("\nEnd of the retrieving request for booking.\n");
        }
        catch (IOException ioe) {
            pw.println("An IOException thrown for unknown reason.");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }

    private void allScheduleGivenRoom() {
        try {
            pw.println("Which room's schedule would you like to check?");
            printCurrentRooms(bookingSystem.getRooms());
            pw.println("Enter your option, '0' to interrupt the action: ");
            int chosenRoom = Integer.parseInt(br.readLine());
            if (chosenRoom == 0) {
                pw.println("Action interrupted!");
                return;
            }

            List<Room> rooms = bookingSystem.getRooms();
            List<Booking> bookings = bookingSystem.getBookings();
            Room room = rooms.get(chosenRoom - 1);

            pw.println("Here is the date today: " + LocalDate.now());
            pw.println("And the time for now: " + LocalTime.now());
            pw.println("The reservation information printed below (if any) " +
                    "indicates that the room is occupied at the time.\n" +
                    "Apart from that, from now on, the room is free .");
            for (Booking booking: bookings) {
                if (room.equals(booking.getRoom())) {
                    pw.print(booking.getInfo());
                }
            }
        }
        catch (IOException ioe) {
            pw.println("An IOException thrown for unknown reason.");
        }
        catch (NumberFormatException nfe) {
            pw.println("Not a valid integer option!");
        }
        catch (IndexOutOfBoundsException outOfBoundsException) {
            pw.println("The option entered is out of the range of displayed options!");
        }
    }

    private void printCurrentPeople(List<Person> people) {
        String name;
        String email;
        pw.println("\nPrinting all current person's info...\n");
        for (int counter = 0; counter < people.size(); counter++) {
            name = people.get(counter).getName();
            email = people.get(counter).getEmail();
            pw.printf("%d. %s <%s>\n", counter + 1, name, email);
        }
    }
    private void printCurrentBuildings(List<Building> buildings) {
        pw.println("\nPrinting all current building's info...\n");
        for (int counter = 0; counter < buildings.size(); counter++) {
            pw.printf("%d. %s\n", counter + 1, buildings.get(counter).getName());
        }
    }
    private void printCurrentRooms(List<Room> rooms) {
        String buildingName;
        String RoomName;
        pw.println("\nPrinting all current room's info...\n");
        for (int counter = 0; counter < rooms.size(); counter++) {
            buildingName = rooms.get(counter).getBuilding().getName();
            RoomName = rooms.get(counter).getName();
            pw.printf("\n%d. Room: %s\n   Building: %s\n", counter + 1, RoomName, buildingName);
        }
    }
    private void printCurrentBookings(List<Booking> bookings) {
        pw.println("\nPrinting all current booking's info...\n");
        for (int counter = 0; counter < bookings.size(); counter++) {
            pw.printf("%d. %s\n", counter + 1, bookings.get(counter).getInfo());
        }
    }


    private void printMainHelp() {
        pw.println(terminalHelp);
    }

    // Print menus for add/remove/models option.
    private void printarmMenu() {
        pw.println(terminalARMMenu);
    }

}
