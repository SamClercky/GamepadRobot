# GamepadRobot

This is a simple cross platform gamepad controller based on lwjgl3 and 100% written in Kotlin. It helps to control games with no gamepad support.

## Functionalities
* Json-config capabilities
* Taking control over the mouse and keyboard but with precaution so the computer doesn't become unhandable
* A lot of possible settings
* Test unit where the gamepad can be tested (useful when creating a new json file)
* Fast reaction by using multithreaded code
* Cross platform (Windows, MacOs, Linux (x64))
## Limitations
* Not all keys are currently availlable
## Contribute
Like a smart roman once said, making mistakes is human, so can I make mistakes and is there always room for improvements.
Please submit a issue or fork this repo.
## How to use
### Installation
Go to the [download page](https://github.com/SamClercky/GamepadRobot/releases) and download the jar-file (it should work on all supported platforms)
Download the file where you want it to install and you are ready to issue commands
On Unix-based systems, make sure you make the file executable
Navigate to the jar-file and issue in the terminal the following command
`sudo chmod +x GamepadRobot.jar`
### Adding a controller
First connect the controller to the computer and make sure there is no other usb-device is connected.
Navigate to the jar-file and issue the command `./GamepadRobot.jar --test`. This starts the application and shows the data from the connected gamepad. Mine looks like this:
```
Event(key=0, value=0, analog=true), Event(key=1, value=0, analog=true), Event(key=2, value=0, analog=true), Event(key=3, value=0, analog=true), Event(key=0, value=0, analog=false), Event(key=1, value=0, analog=false), Event(key=2, value=0, analog=false), Event(key=3, value=0, analog=false), 
-----------------
```
The data gives a update every second. Now hit the buttons of the gamepad and see which value changes and write the key and value down of the Event-closule. It is possible that some buttons use the same key but a different value.
Now we are ready to write the json for our gamepad.
The final will look like this. It is a configuration for a xbox-controller playing Minecraft java edition.
```javascript
{
  "buttons": [
    {"key": "0", "value": "Q"},
    {"key": "0 2", "value": "D"},
    {"key": "1", "value": "Z"},
    {"key": "1 2", "value": "S"},
    {"key": "2", "value": "MOUSEMOVEX"},
    {"key": "2 2", "value": "MOUSEMOVEX"},
    {"key": "3", "value": "MOUSEMOVEY"},
    {"key": "3 2", "value": "MOUSEMOVEY"},
    {"key": "4", "value": "F6"},
    {"key": "4 2", "value": "F7"},
    {"key": "4 3", "value": "F8"},
    {"key": "4 4", "value": "F5"},
    {"key": "5", "value": "MOUSELEFT"},
    {"key": "5 2", "value": "MOUSERIGHT"},
    {"key": "5 3", "value": "SHIFT"},
    {"key": "5 4", "value": "F9"},
    {"key": "6", "value": "E"},
    {"key": "6 2", "value": "ESC"},
    {"key": "6 3", "value": "SPACE"},
    {"key": "6 4", "value": "SPACE"},
    {"key": "7", "value": "F2"},
    {"key": "7 2", "value": "F1"},
    {"key": "7 3", "value": "F4"},
    {"key": "7 4", "value": "F3"}
  ],
  "unmappedBtn" : [
    {"btn": "0", "values": [-1, 1]},
    {"btn": "1", "values": [-1, 1]},
    {"btn": "4", "values": [1, 256, 65536, 16777216]},
    {"btn": "5", "values": [1, 256, 65536, 16777216]},
    {"btn": "6", "values": [1, 256, 65536, 16777216]},
    {"btn": "7", "values": [1, 256, 65536, 16777216]}
  ],
  "mouseSentivity": 50
}
```
If you haven't any experience with json, don't be afraid, just copy mine and commit the changes you prefer.
We will start with the "unmappedBtn". The number after "btn": is just the key value from earlier. Make sure that the number is between the dubble quotes and there are no spaces. The values after "values": are the different values you had for one key. This is used by the program to know which button belongs to which value.
Do this for all your data but not the buttons that will control the mouse movement.
The second section we will discuss is the buttons-section. The key value is the same as the key values from our test but also with a index. The indexes are controlled by the unmappedBtn-section: if there are 2 values, there are also 2 keys with a index, if there are 4 values, ...
The value is the action that has to be bound to the button from the gamepad. Here is a list of all the availlable options. The maining is selfexplaining. (Make sure that the value is always between dubble quotes)

* MOUSEMOVEX
* MOUSEMOVEY
* MOUSELEFT
* MOUSERIGHT
* A
* Z
* E
* R
* T
* Y
* U
* I
* O
* P
* Q
* S
* D
* F
* G
* H
* J
* K
* L
* M
* W
* X
* C
* V
* B
* N
* SHIFT
* CONTROL
* F1 (this is just the 1 on the keyboard and not f1)
* F2
* F3
* F4
* F5
* F6
* F7
* F8
* F9
* F0
* SPACE
* ESC
* NOTHING

Last but not least, there is the mouseSentivity-section. How bigger this number, how faster the mouse will react.
