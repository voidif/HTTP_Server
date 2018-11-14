import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

/**
 * The rate database storage all days information every 10 minutes.
 * The index of every 10 minute from 12:00AM as 0 to 11:59PM as 6 * 24 - 1
 */
public class DataBaseConnection {

//    private static String DB_URL = "jdbc:mysql://localhost/";
//    private static String USER = "root";
//    private static String PASS = "w5684766";
//    private static Connection connection = null;
//    private static Statement statement = null;
    private DatabaseConnectionPool pool = null;

    /**
     * Initialize database setting for a specific thread
     */
    void DataBaseConnection (){
        pool = DatabaseConnectionPool.getInstance();
        pool.init();
    }

    /**
     * Get currency exchange rate by keyword
     * @param keyword current supported keyword: USD_CNY
     * @return currency exchange rate
     */
    public float getRate(String keyword){
        float rate = 0.0f;

        try{
            Connection connection = pool.getConnection();
            Statement statement = connection.createStatement();

            if(keyword.equals("USD_CNY")) {
                String sqldatabase = "USE rate;";
                statement.executeQuery(sqldatabase);

                //get current time index
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int id = hour * 6 + minute / 10;

                String sql = String.format("SELECT * FROM usd_cny WHERE id = %d;", id);
                ResultSet result = statement.executeQuery(sql);


                java.sql.Date sDate = new java.sql.Date(new java.util.Date().getTime());
                //no data, retrieve from internet
                if(result == null || !result.next()) {
                    System.out.println("no value! retrieve from web");
                    rate = GetRate.getUSD_CNY();
                    sql = String.format("INSERT INTO usd_cny VALUES(%d, '%tF', %f);", id, sDate, rate);
                    statement.executeUpdate(sql);
                //old data, query for newest data
                } else if(result.getDate("date").getDate() != new Date().getDate()){
                    System.out.println("value out of date! get new one from web");
                    rate = GetRate.getUSD_CNY();
                    sql = String.format("UPDATE usd_cny SET rate = %f, date = '%tF' WHERE id = %d",
                            rate, sDate, id);
                    statement.executeUpdate(sql);
                //good data
                } else {
                    System.out.println("value is good!");
                    rate = result.getFloat("rate");

                }
            }
        } catch (Exception e){
            System.out.println("Error!!");
            e.printStackTrace();

        }
        return rate;
    }

    private synchronized void updateRate(){

    }
}
