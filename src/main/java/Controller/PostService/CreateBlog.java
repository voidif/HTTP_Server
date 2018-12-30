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

    /**
     *
     * @param response
     * @param url
     * @param body
     * @throws IOException
     */
    public static void storage(SocketChannel response, String url, String body) throws IOException {
//        var msg = {
//                title: this.titleText.value,
//                abstract: this.abstractText.value,
//                content: this.editor.getMarkdown(),
//                file: this.fileName
//        }
        JSONObject paras = new JSONObject(body);
        String blogTitle = (String)paras.get("title");
        String blogAbstract = (String)paras.get("title");
        String blogContent = (String)paras.get("title");
        String blogFile = (String)paras.get("file");

        //create new blog markdown file
        String filePath = CreateBlog.class.getClassLoader().getResource(targetRelativePath).getPath();
        File file = createFile(filePath + "/" + blogTitle);

        byte[] byteArray = blogContent.getBytes();

        //write blog content
        FileOutputStream out = new FileOutputStream(file);
        out.write(byteArray);
        out.flush();
        out.close();

        //write blog json file with the same name

        //test
        transferFile();


        //write back HTTP response
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "\r\n";

        String message = head + "success!";
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

    private static String calculateFileName(String title) {
        return null;
    }
}
