import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPConnectionTest {
    public static void main(String[] args) throws Exception{
        URL url = new URL("http://127.0.0.1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置连接超时为5秒
        conn.setConnectTimeout(5000);
        // 设置请求类型为Get类型
        conn.setRequestMethod("GET");
        // 判断请求Url是否成功
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("请求url失败");
        }
        InputStream inStream = conn.getInputStream();
        byte[] bt = inStream.readAllBytes();
        System.out.println(new String(bt));
    }
}
