package Controller.GetService.JSON;

import Controller.HTTPLibrary;
import org.json.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Dealing JSON related request, For every JSON request, the url
 * must satisfy pattern /json?id=*
 * Parameter Field:
 * id(required)
 */
public class JSONRequestHelper {
    /**
     * Invoke corresponding method based on id
     * @param response
     * @param para key-value pair parameter string
     */
    public static void invokeMethod(SocketChannel response, String para) {
        JSONObject paras = HTTPLibrary.parseParams(para);
        String id = (String) paras.get("id");

        //Using spring to create method bean
        Resource resource = new ClassPathResource("JSONBeans.xml");

        BeanFactory factory = new XmlBeanFactory(resource);
        JSONRequest jsonRequest = (JSONRequest)factory.getBean(id);
        //invoke method
        JSONObject result = jsonRequest.response(paras);
        //write back to response
        try {
            writeJson(response, result);
        } catch (IOException e) {
            System.out.println("JSON method Fail!: " + id);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Return a json object to client.
     * @param response The response that the file will write in.
     * @param json The json to be returned.
     */
    private static void writeJson(SocketChannel response, JSONObject json) throws IOException {
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "Content-Type: application/json" + "\r\n" + "\r\n";

        String message = head.concat(json.toString());
        HTTPLibrary.writeString(response, message.getBytes());
    }
}


