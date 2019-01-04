package Controller.GetService;

import Controller.HTTPLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Dealing with static file get request
 */
public class StaticRequest {

    /**
     * write a specific file to client response according the URL.
     * @param response The response that the file will write in.
     */
    public static void writeStaticFile(SocketChannel response, String url) throws IOException {
        byte[] data = fetchFile(url);
        String ext = getFileExtension(url);
        //make HTTP Head
        StringBuilder head = new StringBuilder("HTTP/1.1 200 OK" + "\r\n");
        //head.append("Connection: keep-alive" + "\r\n");
        head.append("content-length: " + data.length + "\r\n");
        //looking the file extension
        if (ext.equals("html")) {
            head.append("Content-Type: text/html; charset=utf-8" + "\r\n");
        } else if (ext.equals("css")) {
            head.append("content-type: text/css; charset=utf-8" + "\r\n");
        } else if (ext.equals("jpg")) {
            head.append("Content-Type: image/png" + "\r\n");
        } else if (ext.equals("png")) {
            head.append("Content-Type: image/jpeg" + "\r\n");
        } else if (ext.equals("pdf")) {
            head.append("Content-Type: application/pdf; charset=utf-8" + "\r\n");
        } else if (ext.equals("js")) {
            head.append("Content-Type: application/javascript" + "\r\n");
        }
        head.append("\r\n");
        //write into response
        byte[] headArray = head.toString().getBytes();
        byte[] message = new byte[headArray.length + data.length];
        System.arraycopy(headArray, 0, message, 0, headArray.length);
        System.arraycopy(data, 0, message, headArray.length, data.length);
        HTTPLibrary.writeString(response, message);
    }

    /**
     * For a specific file, return the FileInputStream of it.
     * Reminder: All file must under directory /classes/webpage
     * @param url File location
     * @return Byte array for a local file.
     * @throws IOException
     */
    private static byte[] fetchFile(String url) throws IOException {
        try {
            if (url.equals("/")) {
                url = url + "index.html";
            }
            //read file
            url = "webpage" + url;
            String htmlPath = StaticRequest.class.getClassLoader().getResource(url).getPath();
            File file = new File(htmlPath);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            return data;
        } catch (NullPointerException e) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");//设置日期格式
            System.out.print(df.format(new Date()));// new Date()为获取当前系统时间
            System.out.println("No such file: " + url);
            return new byte[0];
        }
    }

    /**
     * For a URL, return the file extension as a String
     * @param url File location.
     * @return String represents a file's extension.
     */
    private static String getFileExtension(String url) {
        //get file extension and convert it to lower case letter
        String[] tmp = url.split("\\.");
        char[] extArray = tmp[tmp.length - 1].toCharArray();
        for (int i = 0; i < extArray.length; i++) {
            if (extArray[i] <= 'Z' && extArray[i] >= 'A') {
                extArray[i] = (char) (extArray[i] - ('A' - 'a'));
            }
        }
        String ext = new String(extArray);
        return ext;
    }
}
