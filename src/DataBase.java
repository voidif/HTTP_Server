import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBase {
    public static String DB_URL = "jdbc:mysql://localhost/test";
    public static String USER = "root";
    public static String PASS = "w5684766";
    public static void main(String args[]){
        Connection connection = null;
        Statement statement = null;
        try{
            //STEP 1: Register the driver
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 3: Execute a query
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql = "SELECT * FROM suppliers;";
//            statement.executeQuery(sql);
//            sql = "SELECT * FROM suppliers;";
            ResultSet result = statement.executeQuery(sql);
            //STEP 4: Display
            while(result.next()){
                System.out.println(result.getInt("sno") + " " + result.getString("sname"));
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
