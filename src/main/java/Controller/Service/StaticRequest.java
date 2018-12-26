package Controller.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Dealing with static file get request
 */
public class StaticRequest {

    /**
     * write a specific file to client response according the URL.
     * @param response The response that the file will write in.
     */
    public static void writeStaticFile(OutputStream response, String url) throws IOException {
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
        response.write(head.toString().getBytes());
        response.write(data);
    }

    /**
     * For a specific file, return the FileInputStream of it.
     * @param url File location
     * @return Byte array for a local file.
     * @throws IOException
     */
    private static byte[] fetchFile(String url) throws IOException {
        if (url.equals("/")) {
            url = url + "index.html";
        }
        //read file
        url = "../webpage" + url;
        String htmlPath = StaticRequest.class.getResource(url).getPath();
        File file = new File(htmlPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return data;
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
