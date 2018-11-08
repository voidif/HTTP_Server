import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseTest {
    private static String DB_URL = "jdbc:mysql://localhost/";
    private static String USER = "root";
    private static String PASS = "w5684766";

    public static void main(String args[]) throws Exception {

        //STEP 1: Register the driver
        Class.forName("com.mysql.jdbc.Driver");
        //STEP 2: Open a connection
        System.out.println("Connecting to database...");
        Connection connection = DriverManager.getConnection(DB_URL,USER,PASS);
        System.out.println(connection.getAutoCommit());

        DatabaseMetaData metaData = connection.getMetaData();
        int driverMaxConnections = metaData.getMaxConnections();


        //STEP 3: Execute a query
        for (int i = 0; i < 10; i ++){
            Statement statement = connection.createStatement();
        }
    }
}


