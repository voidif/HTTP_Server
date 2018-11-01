import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

//Singleton
public class DataBaseConnection {
    //the singleton
    private static DataBaseConnection instance = null;
    //private construct function
    private DataBaseConnection(){}

    //Get unique singleton
    public static synchronized  DataBaseConnection getInstance(){
        if (instance == null) {
            instance = new DataBaseConnection();
            instance.init();
        }
        return instance;
    }

    private static String DB_URL = "jdbc:mysql://localhost/";
    private static String USER = "root";
    private static String PASS = "w5684766";
    private static Connection connection = null;
    private static Statement statement = null;

    private static void init(){
        try{
            //STEP 1: Register the driver
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 3: Execute a query
            System.out.println("Creating statement...");
            statement = connection.createStatement();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //get rate value
    public static float getValue(String keyword){
        if(keyword.equals("USD_CNY")){
            String sqldatabase = "USE rate";
            statement.executeQuery(sqldatabase);
            String sql = "SELECT value FROM usd_cny ORDER BY DATE DESC LIMIT 1;";
//            statement.executeQuery(sql);
//            sql = "SELECT * FROM suppliers;";
            ResultSet result = statement.executeQuery(sql);
            //STEP 4: Display
            while(result.next()){
                System.out.println(result.getInt("sno") + " " + result.getString("sname"));
            }
        }
    }
}
