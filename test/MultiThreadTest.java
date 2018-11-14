import java.awt.event.TextEvent;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.sql.Date;

public class MultiThreadTest{
    public static void main(String args[]) throws Exception {
        //multi thread test
        for (int i = 0; i < 100; i ++){
            new TestThread().start();
        }
        Thread.sleep(2000);
        System.out.println("good: " + TestThread.good);
        System.out.println("bad: " + TestThread.bad);
    }
}

class TestThread extends Thread{
    public static int good = 0;
    public static int bad = 0;
    @Override
    public void run() {
        try{
            URL url = new URL("http://127.0.0.1/");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                good ++;
                System.out.println("Good");
            } else {
                System.out.println("Bad");
                bad ++;
            }
        } catch (Exception e){
            System.out.println("exception, bad!");
        }
    }
}
