
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CharsetTest {
    public static void main(String args[]){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");//设置日期格式
        System.out.print(df.format(new Date()));// new Date()为获取当前系统时间
        System.out.println(Charset.defaultCharset().name());
    }
}
