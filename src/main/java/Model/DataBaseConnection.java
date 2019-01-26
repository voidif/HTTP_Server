package Model;

import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;

/**
 * This class controls all database related activities.
 * The rate database storage all days information every 10 minutes.
 * The index of every 10 minute from 12:00AM as 0 to 11:59PM as 6 * 24 - 1
 */
public class DataBaseConnection {

//    private static String DB_URL = "jdbc:mysql://localhost/";
//    private static String USER = "root";
//    private static String PASS = "w5684766";
//    private static Connection connection = null;
//    private static Statement statement = null;
    private static DatabaseConnectionPool pool = null;
    private static String Redis_URL = "localhost";

    /**
     * Initialize database setting for a specific thread
     */
    public static void init (){
        pool = DatabaseConnectionPool.getInstance();
    }

    /**
     * Get currency exchange rate by keyword
     * @param keyword current supported keyword: USD_CNY
     * @return currency exchange rate
     */
    public static float getRate(String keyword){
        float rate = 0.0f;
        Connection connection = null;
        try{
            connection = pool.getConnection();
            Statement statement = connection.createStatement();

            if(keyword.equals("USD_CNY")) {
//                String sqldatabase = "USE rate;";
//                statement.executeQuery(sqldatabase);

                //get current time index
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int id = hour * 6 + minute / 10;

                String querySQL = String.format("SELECT * FROM usd_cny WHERE id = %d;", id);
                ResultSet result = statement.executeQuery(querySQL);

                java.sql.Date sDate = new java.sql.Date(new java.util.Date().getTime());

                //no data, retrieve from internet
                if(result == null || !result.next()) {
                    System.out.println("no value! retrieve from web");
                    rate = GetRate.getUSD_CNY();
                    String updateSQL = String.format("INSERT INTO usd_cny VALUES(%d, '%tF', %f);", id, sDate, rate);
                    updateRate(statement, querySQL, updateSQL);
                //old data, query for newest data
                } else if(result.getDate("date").getDate() != new Date().getDate()){
                    System.out.println("value out of date! get new one from web");
                    rate = GetRate.getUSD_CNY();
                    String updateSQL = String.format("UPDATE usd_cny SET rate = %f, date = '%tF' WHERE id = %d",
                            rate, sDate, id);
                    updateRate(statement, querySQL, updateSQL);
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
        pool.releaseConnection(connection);
        connection = null;
        return rate;
    }

    /**
     *
     * @param statement The statement used to executed sql
     * @param querySQL Query sql, check whether data has been written into database by another thread
     * @param updateSQL Actually update sql
     */
    private static synchronized void updateRate(Statement statement, String querySQL, String updateSQL){
        try {
            ResultSet result = statement.executeQuery(querySQL);
            //No data or older date in database, update by new one
            if(!result.next() || result.getDate("date").getDate() != new Date().getDate()){
                statement.executeUpdate(updateSQL);
            }
        } catch (SQLException e){
            System.out.println("Update Fail!!");
        }
    }

    /**
     * Storage accept ip to redis database
     * @param address Redis database Address
     */
    public static void storageIP(InetSocketAddress address) {
        Jedis jedis = new Jedis(Redis_URL);
        String ip = address.getAddress().getHostAddress();

        String countNum = jedis.get(ip);
        int count = 0;
        if(countNum != null) {
            count = Integer.parseInt(countNum);
        }
        count ++;

        jedis.set(ip, String.valueOf(count));
    }

}
