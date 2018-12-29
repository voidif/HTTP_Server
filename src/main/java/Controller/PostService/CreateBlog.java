package Controller.PostService;

import Controller.HTTPLibrary;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class CreateBlog {
    private static final String srcRelativePath = "../../src/main/resources/webpage/blogs";
    private static final String targetRelativePath = "webpage/blogs";

    public static void storage(SocketChannel response, String url, String body) throws IOException {
        String para = url.substring(6);
        JSONObject paras = HTTPLibrary.parseParams(para);
        String blogTitle = (String)paras.get("title");

        //create new file
        String filePath = CreateBlog.class.getClassLoader().getResource(targetRelativePath).getPath();
        File file = createFile(filePath + "/" + blogTitle);

        byte[] blogContent = ((String)paras.get("content")).getBytes();

        //write blog content
        FileOutputStream out = new FileOutputStream(file);
        out.write(blogContent);
        out.flush();
        out.close();

        //test
        transferFile();


        //write back HTTP response
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "\r\n";

        String message = head.concat(body);
        HTTPLibrary.writeString(response, message.getBytes());
    }

    private static File createFile(String destFileName) throws IOException{
        File file = new File(destFileName);
        //if file exist or file is a directory or parent folder is not existed
        //throw exception
        if(file.exists() ||
                destFileName.endsWith(File.separator) ||
                !file.getParentFile().exists()) {
            throw new IOException();
        } else {
            file.createNewFile();
        }
        return file;
    }

    private static void transferFile() {

    }
}
