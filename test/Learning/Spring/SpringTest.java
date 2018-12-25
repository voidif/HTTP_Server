package Learning.Spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class SpringTest {
    public static void main(String[] args) {
        try {
//            Resource resource = new ClassPathResource("/Learning/Spring/Beans.xml");
//            String out = resource.getURL().toString();
//            System.out.println(out);
//            BeanFactory factory = new XmlBeanFactory(resource);
//            HelloWorld obj = (HelloWorld)factory.getBean("HelloWorld");
            Class my = Class.forName("Learning.Spring.helloWorld");
            my.getConstructor().newInstance();
            ApplicationContext context =
                    new FileSystemXmlApplicationContext("/target/test-classes/Learning/Spring/Beans.xml");
            helloWorld obj = (helloWorld)context.getBean("HelloWorld");
            obj.getMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
