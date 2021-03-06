package Model;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetRate {
    private static String API_KEY = "1fc497b26127f4a176d8";

    public static float getUSD_CNY() throws Exception{
        URL url = new URL("http://free.currencyconverterapi.com/api/v5/convert?q=USD_CNY&compact=y&apiKey=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.connect();
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Connect Error!");
        } else {
//            System.out.println(conn.getContentType());
            InputStream in = conn.getInputStream();
            byte[] data = in.readAllBytes();
            String jsonString = new String(data, "UTF-8");
            JSONObject jObject1 = new JSONObject(jsonString);
            JSONObject jsonObject2 = jObject1.getJSONObject("USD_CNY");
            float value = jsonObject2.getFloat("val");
//            System.out.println(value);
            return value;
        }

    }
}
