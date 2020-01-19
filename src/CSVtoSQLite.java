import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.io.Reader;
import java.io.FileWriter;
import java.util.logging.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
/**
 * Class to parse CSV file.
 * Completed Records are entered into an SQLite Database.
 * Incomplete Records are entered into a new CSV file.
 * Number of total, successful, and failed records logged.
 * 
 * Utilizes Apache Commons CSV library for CSV reading/writing (version 1.7).
 * Utilizes Java SQLite JDBC Driver (version 3.30.1).
 * 
 * @author Abdul Basit
 */
public class CSVtoSQLite {
	/**
	 * Main method.
	 * Consumes CSV file, and seperates records into SQLite DB and CSV file as necessary.
	 * Logs record numbers.
	 * 
	 * @param args - standard command line arguments
	 */
	public static void main(String[] args) {
		//if no file name is given when running application, throws RuntimeException
		if(args.length == 0)
			throw new RuntimeException("Invalid use, argument for filename required.");
		
		//declare and initialize integers for record numbers, Logger, and various StringBuilders for file names
		int totalRecords = 0, failedRecords = 0, successRecords = 0;
		final String FILENAME = args[0];
		StringBuilder badFileName = new StringBuilder(FILENAME.substring(0, args[0].length()-4));
		StringBuilder dbFileName = new StringBuilder(badFileName);
		StringBuilder logFileName = new StringBuilder(badFileName);
		badFileName.append("-bad.csv");
		dbFileName.append(".db");
		logFileName.append(".log");
		Logger log = Logger.getLogger("CSVtoSQLite");
		
		//try catch block containing file parsing and writing
		try {
			//declare and initialize CSVParser, FileWriter, CSVPrinter, Connection, and PreparedStatement
			Reader reader = Files.newBufferedReader(Paths.get(FILENAME));
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
			FileWriter writer =  new FileWriter(badFileName.toString());
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
			csvPrinter.printRecord(csvParser.getHeaderNames());
			//creates new SQLite DB with 1 table and 10 columns a through j.
			Connection conn = createTable(dbFileName.toString());
			String sql = "INSERT INTO GOOD_RECORDS(a, b, c, d, e, f, g, h, i, j) VALUES(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			System.out.println("Parsing CSV file...");
			//Iterates through each record in CSV file
			for(CSVRecord record : csvParser) {
				totalRecords++;
				ArrayList<String> clientRecord = new ArrayList<>();
				boolean isValidRecord = true;
				//adds each element of current record to ArrayList, checks for missing data in each record
				for(int i = 0; i < 10; i++) {
					clientRecord.add(record.get(i));
					if(record.get(i).isEmpty())
						isValidRecord = false;
				}
				//if record contains missing data, writes record to new CSV file
				if(!isValidRecord) {
					csvPrinter.printRecord(clientRecord);
					failedRecords++;
				}		
				//if record contains all necessary data, writes record to SQLite DB
				else {
					for(int i = 0; i < 10; i++)
						pstmt.setString(i + 1, clientRecord.get(i));
					pstmt.executeUpdate();
					successRecords++;
				}
			}
			
			//closes Parser, Printer, Reader, Connection, and PreparedStatement
			csvParser.close();
			csvPrinter.close();
			reader.close();
			conn.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//sets up FileHandler for Logger log, and logs info regarding number of records handled to log file
		setUpHandler(log, logFileName.toString());
		log.info("Total Records Processed: " + totalRecords + "\n\t  " +
				 "Successful Records: " + successRecords + "\n\t  " + 
				 "Failed Records: " + failedRecords);
		
		System.out.println("Process Completed.");
	}
	
	/**
	 * Method to establish connection to/create an SQLite database.
	 * Creates new table in database with 10 columns named "GOOD_RECORDS".
	 * Returns connection to database.
	 * 
	 * @param dbName - name for existing/new database
	 * @return c - connection to database
	 */
	public static Connection createTable(String dbName) {
		Connection c = null;
		Statement st = null;
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			System.out.println("SQLite DB Connected");
			
			st = c.createStatement();
			String sql = "CREATE TABLE GOOD_RECORDS	" + 
						   "(A		TEXT		NOT NULL," +
						   " B		TEXT		NOT NULL," +
						   " C		TEXT		NOT NULL," +
						   " D		TEXT		NOT NULL," +
						   " E		TEXT		NOT NULL," +
						   " F		TEXT		NOT NULL," +
						   " G		TEXT		NOT NULL," +
						   " H		TEXT		NOT NULL," +
						   " I		TEXT		NOT NULL," +
						   " J		TEXT		NOT NULL)";
			st.executeUpdate(sql);
			st.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		return c;
	}
	
	/**
	 * Method to create FileHandle and attach to logger.
	 * FileHanlder level is set to info.
	 * FileHanlder format set to simple.
	 * 
	 * @param log - logger to utilize FileHanlder
	 * @param logFileName - name of output log file
	 */
	public static void setUpHandler(Logger log, String logFileName) {
		LogManager.getLogManager().reset();
		try {
			FileHandler fh = new FileHandler(logFileName);
			fh.setLevel(Level.INFO);
			fh.setFormatter(new SimpleFormatter());
			log.addHandler(fh);
		} catch(Exception e) {
			System.out.println("File logger failure");
			System.out.println(e);
		}
	}
}