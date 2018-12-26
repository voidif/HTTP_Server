package Learning.Spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class SpringTest {
    public static void main(String[] args) {
        try {
            Resource resource = new ClassPathResource("/Learning/Spring/Beans.xml");
            BeanFactory factory = new XmlBeanFactory(resource);
            helloWorld obj = (helloWorld)factory.getBean("HelloWorld");

            obj.getMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
