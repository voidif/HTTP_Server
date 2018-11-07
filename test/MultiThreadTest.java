import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.sql.Date;

public class MultiThreadTest{
    public static void main(String args[]) throws Exception {
        //multi thread test
        for (int i = 0; i < 100; i ++){
            new Thread(){
                @Override
                public void run() {
                    try{
                        URL url = new URL("http://127.0.0.1/");
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setRequestMethod("GET");
                        conn.connect();
                        if (conn.getResponseCode() == 200) {
                            System.out.println("good!");;
                        } else {
                            System.out.println("bad!");
                        }
                    } catch (Exception e){
                        System.out.println("exception, bad!");
                    }
                }
            }.start();

        }
    }
}
