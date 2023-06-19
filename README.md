# Room Booking System
*15 Jun 2023*

**_This mini-project is developed under jdk 17.0.3, and it has been confirmed that all the implemented functions can be run and tested in this environment._**



## Overview

In the final stage of developing and testing, the project could be said to meet all the functional requirements
listed on the specification with appropriate error handling.
Users can easily find the detailed help information
or instructions that are highly readable in both graphical user interface and text based interface.

Regarding the mechanism of making booking and removing records,
the project could be described as **_do not look back_**.
For making a booking, the entered date and time will be regarded as valid if it is later than the moment
the request is sent, and then, it will be passed to the subsequent logical judgment method.
(whether the room has been reserved in the same time block)
So, only those booking that had already past and
not removed from the system can be retrieved from the system.

It is worthwhile to explain the **Remove** operation of the project here.
Although different developers may have different implementations,
this layer of logical operations should be common and must be implemented.
When the user sends a request to remove a model that are logically connects to another existing model,
for example, removing a person with a booking event to a room, both of UIs would prompt the user that
the deletion of such an entity will result in the elimination of the corresponding logical connection or reference.
But, if the user insists on removing it, going back to the example,
all rooms booked by the person will be dereferenced from the booking made by that person(previous booking included),
then all bookings issued by the person will be removed
from the system, and finally, the person's information will be removed from the system.



## Extra Features
This feature is divided into four sub-options, as are the four distinguishable models included in the system.
It is the fifth option for the **view** option in the text-based interface and the last menu item of
the **File** menu in the graphical user interface,
viewing all current records of a model. (Person, Building, Room, Booking)

Maybe it could not be counted as an extra feature, a file named ```test.ser``` which store some entries(overlaps with
some information in reality, not sure will it be offended) was also submitted,
the functionality and the synchronisation
of two UIs could be tested by following the instructions in the TUI.(or opened it in GUI directly as the default
directory for both UIs' loading and saving was set to ```./src/logs/```)



## How to compile and run in Lab Linux Machine
At the root directory of this project, which contains ```/src``` and this ```README.md``` file,
to compile all necessary component .java files that enables the program to behave as described above,
run: ```javac src/delegateView/*.java src/main/*.java src/model/*.java``` in Linux command line.
Then, at the same level, to open the GUI and TUI, run ```src.main.Main``` in Linux command line.



## Instructions to run unit tests and some reflection

Due to the limitation of time, most of my effort of compiling and building an appropriate sets of Junit test file
finally failed, I might have been able to accomplish it if I had sufficient advance experience of depth of how to
write shell script and JUnit tests file.

I wrote some basic Unit tests which produce the expected output, works fine in both IntelliJ and Linux terminal.
In Linux terminal, compile the test files by entering:
``javac -cp .:./src/JUnit/junit.jar:./src/JUnit/hamcrest.jar:. ./src/JUnit/SystemTests/*.java``,
and run with
``java -cp .:./src/JUnit/junit.jar:./src/JUnit/hamcrest.jar:. src.JUnit.SystemTests.SystemTestSuiteRunner``
.

With my original design rationale, although some features appear to be more user-friendly,
such as the users only need to enter the serial number rather than the
string that matches every singles characters to the building name when adding or removing dependent instance,
such as rooms that depend on the existence of a building.

Generally speaking, due to lack of development experience, by the time this project was submitted,
I was not able to successfully encapsulate some functionality in the object defined inside the ``./src/model`` folder,
but rather in two terminal programs in the ``./src/view`` folder. 

Additionally, combining the above design philosophy with my relatively weak development skills at the time,
I set the formal parameters of the constructor of an instance to its dependent instance (if any), for example,
the initialisation of a Room instance would need a Building instance to be passed in. Also, between those
interconnected instances, I set some reference fields in each of them as I thought
it would be helpful to retrieve the dependent information related to this instance directly.
However, the setting of these fields seems redundant to me now,
as such searches for relevant information should be encapsulated in the ``BookingSystem`` object.

In all, the choice of design increases the degree of difficulty in writing a complete set of test documents.


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
