import Controller.HTTP_Server;
import Model.MyBatis.Test;

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
        try {
            Test test = (Test)session.selectOne("Model.MyBatis.TestMapper.selectTestById", 1);
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println("IO Error!!!");
            e.printStackTrace();
        } finally {
            session.close();
        }

    }
}
