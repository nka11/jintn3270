JinTN3270
---------
JinTN3270 Is a 3270 Terminal emulator, written in 100% Java, licensed under 
the terms of the OSI approved BSD license.

JinTN3270 is a clean-room implementation of RFCs:
	854, 856, 858, 885, 1041, 1576, and 1647.

Additional information was gleaned from IBM Document Number GA23-0059-07, 
3270 Information Display System Data Stream Programmer's Reference.

The GUI component (JTerminal) is an evolved version of the one I created for 
FreeHost3270. It has changed significantly from it's humble beginnings there.
For many reasons (both technical and practical) I have deemed FreeHost3270 
unfit for general consumption, and desire to replace in my own projects.

That's why JinTN3270 started. FreeHost3270 just couldn't cut it for me when 
it came to performance and stability. It's threading model is flakey, it's 
parser delegation horrendous, and it's client code unmaintainable.