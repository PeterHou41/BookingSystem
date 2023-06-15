package src.delegateView;

/** Includes help messages used by both UIs. */
public class SharedConstants {

    private static final String terminalPrefix =
            "\nApart from \"Exit\", " +
            "selecting each option brings up a sub-menu which contains subsequent options\n";

    private static final String graphicalPrefix =
            "\nTo interact with the graphical user interface of the simple Room Booking System,\n" +
                    "\nclick on the one of the menu bars at the top of the interface and\n" +
                    "\nclick the subsequent menu item to send a request.\n" +
                    "\nThen, according to the prompts that appear in the centre of the interface,\n"+
                    "\nenter the information in the text box at the bottom of the interface\n";

    private static final String sharedHelpContent =
            "\nAdd: Adding a person, building, room or booking to the system with valid input\n" +
            "\nRemove: Removing a record from the system for one of the four entities mentioned above\n" +
            "\nView: Five subsequent options will be given in the sub-options\n" +
            "\n      All rooms available at a given (typed in) time (point)\n" +
            "\n      All rooms available for a given time period\n" +
            "\n      All booking made by a given person\n" +
            "\n      The schedule including bookings and free periods for a given room\n" +
            "\n      All records of a type of entity registered to the system\n" +
            "\n      Namely people, buildings, rooms or bookings\n";



    // Load, Save and Exit message for terminal.
    private static final String terminalLSE =
            "\nLoad: Load a log file that stored states of system\n" +
            "\nSave: Save the current state of the system to a log file\n" +
            "\nExit: Exit this text-based UI\n";


    // File(Load, Save) and Exit message for GUI.
    private static final String graphicalLSE =
            "\nFile: Load or save a file that stores states of the system from/to local file\n" +
            "\nExit: Click the button on the top right with icon \"x\" and then\n" +
            "\nExit: click \"Yes\" in the subsequent confirmation.\n";

    private static final String graphicalNotice =
            "\nYou are recommended to save and load the state file of the system at\n" +
            "\nthe sub-directory </logs> of <src>, the parent directory of\n" +
            "\n</main>, </model> and </view>, and this would also be the default folder that\n" +
            "\nopens when choosing one of the File options.\n";
    // .ser ?
    private static final String terminalNotice =
            "\nConsidering the tedium of typing the long local address,\n" +
            "\nloaded and saved files would be stored at the sub-directory\n"+
            "\n</logs> of <src>, the parent directory of /main, /model and /view.\n" +
            "\nSpecific instructions will be printed after choosing \"Load\" or \"Save\"\n";

    private static final String startOfNotice = "\n########################### Notice ############################\n";
    private static final String endOfNotice = "\n######################## End of Notice ##########################\n";

    /** Help message box for terminal UI. */
    public static final String terminalHelp =
            terminalPrefix + sharedHelpContent + terminalLSE +
            startOfNotice + terminalNotice + endOfNotice;

    /** Help message box for graphical UI. */
    public static final String graphicalHelp =
            graphicalPrefix + sharedHelpContent + graphicalLSE +
            startOfNotice + graphicalNotice + endOfNotice;

    /** Information about logics behind how the system runs for GUI. */
    public static final String logic = "\nFor adding a booking and viewing options that requires user's input of time,\n" +
            "\napart from entering the date and time in the format prompted by the interface,\n" +
            "\nthe entered time will be treated valid if and only if it is later than the time\n" +
            "\npoint when the request is sent, the validation process of entered date is also the same.\n" +
            "\nIt is not possible to directly create entities that logically depend on the existence of\n" +
            "\nanother entity with the absence of a dependent entity. For instance, a room could not be\n" +
            "\nadded to the system if there is no building registered in the system.\n";

    private static final String terminalAdditional =
            "5. Back to the previous level of menu\n"+
            "Enter your option: ";

    /** Prepared for GUI to implement the additional interactive functionality. */
    public static final String graphicalarmMenu =
            "Which type of entity/event would you like to add to/remove/view from the system?\n" +
            "1. Person\n" +
            "2. Building\n" +
            "3. Room\n" +
            "4. Booking\n" ;



    /** Menu for additional viewing functionality for TUI. */
    public static final String terminalARMMenu = graphicalarmMenu + terminalAdditional;

    /** Date pattern for input validity check shared by both UI. */
    public static final String datePattern = "yyyy-MM-dd";

    /** Time pattern for input validity check shared by both UI. */
    public static final String timePattern = "HH:mm";

    }
