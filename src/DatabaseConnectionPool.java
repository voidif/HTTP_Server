import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A Database Connection pool used to store all connections.
 */
public class DatabaseConnectionPool {
    private static DatabaseConnectionPool connectionPool = null;
    public static synchronized DatabaseConnectionPool getInstance(){
        if (connectionPool == null){
            connectionPool = new DatabaseConnectionPool();
        }
        return connectionPool;
    }

    private String DB_URL = "jdbc:mysql://localhost/rate";
    private String USER = "root";
    private String PASSWORD = "w5684766";
    private String TEST_TABLE = "test";
    /**
     * Default initial capacity.
     */
    private int DEFAULT_CAPACITY = 10;
    /**
     * Default maximum capacity.
     */
    private int MAX_CAPACITY = 50;
    private ArrayList<pooledConnection> pool = null;

    public void init(){
        //create connections
        if(pool == null){
            createConnectionPool();
        }
    }

    /**
     * create maxConnection number connections
     */
    private void createConnectionPool(){
        //check the pool object, make sure it has maxConnection numbers of connection
        if(pool == null){
            pool = new ArrayList<>();
        }
        for(int i = pool.size(); i < DEFAULT_CAPACITY; i ++){
            pool.add(createPooledconnection());
        }
    }

    /**
     * Create a new single pooled connection
     * @return a new pooled connection
     */
    private pooledConnection createPooledconnection(){
        pooledConnection newConnection = new pooledConnection(DB_URL, USER, PASSWORD);
        return newConnection;
    }

    /**
     * Get an idle connection from the pool, if there is none, try to
     * create more connections.
     * @return a connection
     */
    public synchronized Connection getConnection() throws SQLException{
        Connection returnConnection = findConnection();
        if(returnConnection == null){
            increaseConnection(5);
            returnConnection = findConnection();
        }
        return returnConnection;
    }

    private void increaseConnection(int addNum){
        int finalCapacity = Math.min(MAX_CAPACITY, pool.size() + addNum);
        for(int i = pool.size(); i < finalCapacity; i ++){
            pool.add(createPooledconnection());
        }
    }


    /**
     * Release a connection to connection pool
     * @param con the connection will be released
     */
    public void releaseConnection(Connection con){
        for(pooledConnection tmp : pool){
            if(tmp.getConnection().equals(con)){
                tmp.setBusy(false);
                return;
            }
        }
        System.out.println("Bad connection can not be returned!");
    }


    /**
     * Try to find an idle connection from the pool
     * @return a connection
     */
    private Connection findConnection() throws SQLException{
        Connection newConnection = null;
        //Looking through all connection in the pool to find one
        for(pooledConnection tmp : pool){
            if(!tmp.isBusy()){
                if(tmp.isValid(TEST_TABLE)){
                    newConnection = tmp.getConnection();
                    break;
                } else {
                    tmp.refreshConnection();
                    newConnection = tmp.getConnection();
                }
            }
        }
        return newConnection;
    }
}

/**
 * A class that contains really connection to database.
 * Using different function to manager connection in it.
 */
class pooledConnection{
    /**
     * true SQL connection
     */
    private Connection connection = null;
    /**
     * busy flag
     */
    private boolean busy = true;
    private String DB_URL = "jdbc:mysql://localhost/";
    private String USER = "root";
    private String PASSWORD = "w5684766";


    public pooledConnection(String DB_URL, String USER, String PASSWORD){
        try{
            this.DB_URL = DB_URL;
            this.USER = USER;
            this.PASSWORD = PASSWORD;
            connection = DriverManager.getConnection(DB_URL,USER,PASSWORD);
            busy = false;
        } catch (SQLException e){
            System.out.println("ERROR: Create connection failed！！");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    private void closeConnection(){
        try{
            connection.close();
        } catch (SQLException e){
            System.out.println("Close Fail!");
        }
    }

    /**
     * A function to check whether current connection is still valid,
     * using test table name, try to select count(*) from that table
     * @return boolean, if current connection valid and useful
     */
    public boolean isValid(String testTableName){
        //try to query a test table to test the connection
        try{
            Statement testStat = connection.createStatement();
            testStat.execute(String.format("SELECT * FROM %s;", testTableName));
        } catch(SQLException e){
            System.out.println("Connect test fail! Not valid");
            e.printStackTrace();
            //close connection
            closeConnection();
            return false;
        }
        return true;
    }

    /**
     * Refresh connection, try to create a new connection, replace the old one
     */
    public void refreshConnection() throws SQLException{
        if(isBusy()){
            System.out.println("Connection busy! Can not refresh");
            throw new SQLException();
        }
        try{
            connection = DriverManager.getConnection(DB_URL,USER,PASSWORD);
            busy = false;
        } catch (SQLException e){
            System.out.println("ERROR: Create connection failed！！");
        }
    }
}