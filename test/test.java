import java.util.Calendar;
import java.sql.Date;

public class test{
    public static void main(String args[]) throws InterruptedException {
        java.sql.Date sDate = new java.sql.Date(new java.util.Date().getTime());
        String sql = String.format("INSERT INTO usd_cny VALUES(%d, '%tF', %f);", 1, sDate, 6.6f);
        System.out.println(sql);

    }
}
