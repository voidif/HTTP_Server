import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

//singleton pattern
public class DatabaseConnectionPool {
    private static DatabaseConnectionPool connectionPool = null;
    public static synchronized DatabaseConnectionPool getInstance(){
        if (connectionPool == null){
            connectionPool = new DatabaseConnectionPool();
            connectionPool.init();
        }
        return connectionPool;
    }

    private String DB_URL = "jdbc:mysql://localhost/";
    private String USER = "root";
    private String PASS = "w5684766";
    private int maxConnection = 10;
    private ArrayList<pooledConnection> pool = null;

    private void init(){
        //create connections
        //TO DO
    }

    //create  maxConnection number connections
    private void createConnection(){
        //check the pool object, make sure it has maxConnection numbers of connection
        if(pool == null){
            pool = new ArrayList<>();
        }
        for(int i = pool.size(); i < maxConnection; i ++){
            pool.add(createPooledconnection());
        }
    }

    private pooledConnection createPooledconnection(){
        pooledConnection newConnection = new pooledConnection();
        return null;
    }

    class pooledConnection{
        /**
         * true SQL connection
         */
        private Connection connection = null;
        /**
         * busy flag
         */
        private boolean busy = true;

        public pooledConnection(){
            try{
                connection = DriverManager.getConnection(DB_URL,USER,PASS);
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

        /**
         * A function to check whether current connection is still valid
         * @return boolean, if current connection valid and useful
         */
        public boolean isValid(){
            //TODO
            return false;
        }

    }
}
