import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBaseConnectionPoolTest {
    public static void main(String[] args){
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        pool.init();
        try{
            Connection test = pool.getConnection();
            Statement statement = test.createStatement();
            String sql = "SELECT * FROM test";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                System.out.println(resultSet.getInt(1));
            }
            pool.releaseConnection(test);

        } catch (Exception e){
            System.out.println("Fail!");
            e.printStackTrace();
        }

    }
}
