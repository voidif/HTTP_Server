import java.util.Calendar;
import java.util.Date;

public class test{
    public static void main(String args[]) throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        System.out.println(calendar.getTime());
        System.out.println(hour + " " + minute);
        Date date1 = new Date();
        System.out.println(date1.toString());
        //毫秒
        Thread.currentThread().sleep(6000);
        Date date2 = new Date();

        System.out.println(date2.toString());
        System.out.println(date1.before(date2));
        System.out.println(date1.getDay() + " " + date2.getDay());
    }
}
