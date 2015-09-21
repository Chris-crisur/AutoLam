Everyone Look at the Tutor interface GUI. I am not sure whether i should include the text area on the right where the tutor can see the students report.
Currently what it does is read in the file. loads it. and prints out the student on the table. I am not yet done with the other UI I am still working on it. I will be done soon soon
Please let me know ASAP if I should include the report section on the right. I need to know so that i can finalize UI in order to put the User manual to rest, The Tutor interface atleast

How to run.
Run the tutor ui. Upload the file solutions.lam using the file chooser then press the submit/upload button. I don't remember what i named it as.


CHRIS
I changed the file you are reading questions from to by the following:

I only changed the first line lol

Before
Student Number

Now

Student Number#Student Name

I am using hash(#) to split the things in order to create Student out of the info

The class Automarker constuctor now takes in a file name as a parameter.
Automarker also outputs a file written ReportXXXX where XXXX is the student number

ANNY

I added mark as one of the student attributes. and a method to update the mark the accepts an int. In case of negative marking the interger parameter will be negative
I also changed the type to wierd type names which i absolutely need for the table to work.

I removed the set methods because there is never an instance where we mudify the name of the user. Only when the student is created and it's only done using the constructor.
