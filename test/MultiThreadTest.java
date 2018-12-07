import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;

import java.awt.event.TextEvent;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.sql.Date;

public class MultiThreadTest{
    public static void main(String args[]) throws Exception {
        //multi thread test
        int total = 1000;
        for (int i = 0; i < total; i ++){
            new TestThread("http://yifu.click/").start();
        }
        //sum up
        Thread.sleep(20000);
        System.out.println("Test Complete:");
        System.out.println("Running Thread Number: " + total);
        System.out.println("Success: " + TestThread.good + ", Failed: " + TestThread.bad);
        System.out.println("Runtime: MAX: " + TestThread.max + ", MIN: " +
                TestThread.min + ", AVG: " + TestThread.totalTime / TestThread.good);
    }
}

class TestThread extends Thread{
    public long runTime = 0;
    public static long max = 0;
    public static long min = Integer.MAX_VALUE;
    public static long totalTime = 0;
    public static int good = 0;
    public static int bad = 0;
    private String urlString;

    public TestThread(String urlString){
        this.urlString = urlString;
    }

    @Override
    public void run() {
        try{
            long startTime = System.currentTimeMillis();
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();
            String type = conn.getContentType();
            InputStream obj = conn.getInputStream();
            String content = new String(obj.readAllBytes());
            if (conn.getResponseCode() == 200) {
                runTime = System.currentTimeMillis() - startTime;
                totalTime += runTime;
                min = Math.min(min, runTime);
                max = Math.max(max, runTime);
                good ++;
            } else {
                bad ++;
            }
        } catch (Exception e){
            //System.out.println("exception, bad!");
            bad ++;
        }
    }
}
