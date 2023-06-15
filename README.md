# CS5001 Practical P4 - Room Booking System
*25 Nov 2022*




## Overview

In the final stage of developing and testing, the project could be said to meet all the functional requirements
listed on the specification with appropriate error handling. Users can easily find the detailed help information
or instructions that are highly readable in both graphical user interface and text based interface.

Regarding the mechanism of making booking and removing records, the project could be described as "do not look back".
For making a booking, the entered date and time will be regarded as valid if it is later than the moment
the request is sent, and then, it will be passed to the subsequent logical judgment method.
(whether the room has been reserved in the same time block) So, only those booking that had already past and
not removed from the system can be retrieved from the system.

It is worthwhile to explain the "Remove" operation of the project here.
Although different developers may have different implementations,
this layer of logical operations should be common and must be implemented.
When the user sends a request to remove a model that are logically connects to another existing model, 
for example, removing a person with a booking event to a room, both of UIs would prompt the user that
the deletion of such an entity will result in the elimination of the corresponding logical connection or reference.
But, if the user insists on removing it, going back to the example, all rooms booked by the person will be dereferenced
from the booking made by that person(previous booking included), then all bookings issued by the person will be removed
from the system, and finally, the person's information will be removed from the system.



## Extra Features
This feature is divided into four sub-options, as are the four distinguishable models included in the system.
It is the fifth option for the "view" option in the text-based interface and the last menu item of the "File" menu
in the graphical user interface, viewing all current records of a model. (Person, Building, Room, Booking)

Maybe it could not be counted as an extra feature, a file named <test.ser> which store some entries(overlaps with 
some information in reality, not sure will it be offended) was also submitted, the functionality and the synchronisation
of two UIs could be tested by following the instructions in the TUI.(or opened it in GUI directly as the default
directory for both UIs' loading and saving was set to ./src/logs/)



## How to compile and run in Lab Linux Machine
At the root directory of this project, which contains /src and this README.md file,
to compile all necessary component .java files that enables the program to behave as described above,
run: <javac src/delegateView/*.java src/main/*.java src/model/*.java> in Linux command line.
Then, at the same level, to open the GUI and TUI, run <src.main.Main> in Linux command line.



## About JUnit test file
Due to the limitation of time, most of my effort of compiling and building an appropriate sets of Junit test file
finally failed, I might have been able to accomplish it if I had sufficient advance experience of depth of how to
write shell script and JUnit tests file.

I wrote a basic Unit test file which works fine in IntelliJ and produce the expected output. But, when I tried to
compile the file with command:
<javac -cp ./src/JUnit/junit.jar:. ./src/JUnit/SystemTests/*.java>
and run with <java -cp ./src/JUnit/junit.jar:./src/JUnit/hamcrest.jar:. org.junit.runner.JUnitCore src.JUnit.SystemTests.SystemTestSuiteRunner>
at the aforementioned level of directory, I would always get a initializationError and a Exception says "No runnable methods",
which is quite confusing.


## References
This README.md file was modified and completed with my special thanks to Prof.Saleem Bhatti as
the implementation was based on the 00-README.md he provided for another module's coursework.
in another module.




## File Structure

| Folder name             | Information                                                                    |
|:------------------------|--------------------------------------------------------------------------------|
| ./                      | Root directory of this project(submission).                                    |
| ./README.md             | This file.                                                                     |
| ./src                   | Contains other sub-directories.                                                |
| ./src/logs              | The directory into which system state files are placed.                        |
| ./src/main              | Contains main method of the project.                                           |
| ./src/model             | Stores .java files that can be logically categorised as model.                 |
| ./src/delegateView      | Stores .java files that can be logically categorised as delegate-view.         |
| ./src/JUnit             | Contains a sub-directory and the necessary dependencies to run the test files. |
| ./src/JUnit/Systemtests | Contains some basic test files.                                                |
