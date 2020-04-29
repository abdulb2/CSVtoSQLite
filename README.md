# CSVtoSQLite
Small Java program to parse CSV file and enter records into an SQLite database

### Summary
* Application to consume a CSV file
* Parses specified CSV file, and checks each record for completeness
* Complete records are entered into an SQLite database
* Incomplete records are writen into a new CSV file
* Log file created at then end of the program, tracking number of total records, complete records, and incomplete records processed

### How To Use
* Copy src and lib folders into a directory
* Navigate to src folder
* Compile on command line, specifying classpath to libs folder containing necessary JAR files
* Example compile command: `javac -cp "../libs/*:." *.java` (MacOS)
* Run program on command line, specifying classpath to libs folder containing necessary JAR files
* Specify CSV file to consume as first command line argument
* Example run command: `java -cp "../libs/*:." CSVtoSQLite example.csv` (MacOS)
* Output .csv, .db, and .log files will be created one directory above src folder.

### Approach, Design, and Assumptions
Prior to writing this program, I had no experience with csv files or SQLite. After reading and learning about the two subjects (in relation to Java), I opted to use Appache Commons CSV to read and write csv files due to its simplicity and efficiency. For the SQLite database, I opted to use Java's SQLite JDBC driver due to its simplicity. I chose to create a new table within the Java program itself, rather than using something like the CLI tool that comes with SQLite, to minimize the amount of software required to utilize this application. 

Assumptions made include:
* The database being created/connected does not already contain a table named "GOOD_RECORDS"
* The user will only attempt to parse CSV files with an appropriate header, containing 10 columns

### Notes
* Although this program is used specifically to parse csv files with 10 columns and a header, minumum changes would need to be made to increase/decrease the total number of columns and the presence/lack of a header
* This program was writen, compiled, and tested using Java 8
