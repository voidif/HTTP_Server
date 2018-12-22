import Controller.HTTP_Server;
import Model.MyBatis.Test;

import Model.MyBatis.TestMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisTest {
    public static void main(String args[]) {
        String resource = "../mybatis-config.xml";
        InputStream inputStream = HTTP_Server.class.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        System.out.println(session.getClass());
        try {
            TestMapper mapper = session.getMapper(TestMapper.class);
            Test test = mapper.selectTestById(1);
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println("IO Error!!!");
            e.printStackTrace();
        } finally {
            session.close();
        }

        try {
            Class<?> aaa = Class.forName("java.lang.String");
            aaa.getConstructor();
            String string = (String)aaa.newInstance();
        } catch (Exception e) {
            System.out.println("error");
        }

    }
}
