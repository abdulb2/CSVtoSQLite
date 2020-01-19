# CSVtoSQLite
Small Java program to parse CSV file and enter records into an SQLite database

### Summary
* Application to consume a CSV file
* Parses specified CSV file, and checks each record for completeness
* Complete records are entered into an SQLite database
* Incomplete records are writen into a new CSV file
* Log file created at then end of the program, tracking number of total records, complete records, and incomplete records processed

### How To Use
* Copy src and lib folder into a directory
* Naviage to src folder
* Compile on command line, specificying classpath to libs folder containg necessary JAR files
* Example compile command: `javac -cp "../libs/*:." *.java` (MacOS)
* Run program on command line, specificying classpath to libs folder containg necessary JAR files
* Specificy CSV file to consume as first command line argument
* Example run command: `java -cp "../libs/*:." CSVtoSQLite example.csv` (MacOS)
* Output .csv, .db, and .log will be created one directory above src folder.
