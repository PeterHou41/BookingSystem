package src.main;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.model.BookingSystem;
import src.delegateView.Terminal;
import src.delegateView.GUI;

/** Contains main method to run the two separate UI together. */
public class Main {

    /** Main method to run GUI and TUI simultaneously. */
    public static void main(String[] args) {

        BookingSystem sharedSystem = new BookingSystem();

        // Open a thread pool for running terminal UI and GUI separately.
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        threadPool.execute(() -> new GUI(sharedSystem));

        threadPool.execute(() -> new Terminal(sharedSystem));

        threadPool.shutdown();
    }
}
