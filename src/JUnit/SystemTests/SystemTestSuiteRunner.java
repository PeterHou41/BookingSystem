package src.JUnit.SystemTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class SystemTestSuiteRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(src.JUnit.SystemTests.SystemTestSuite.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        if (result.wasSuccessful()) {
            System.out.println("Passed all " + result.getRunCount() +" System JUnit tests!");
            System.exit(0);
        }
        else {
            System.out.println("Failed " + result.getFailureCount() + " out of " + result.getRunCount() +" System JUnit tests!");
            System.exit(1);
        }
    }
}
