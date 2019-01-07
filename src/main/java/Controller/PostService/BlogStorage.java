package Controller.PostService;

import Controller.HTTPLibrary;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BlogStorage {
    private static final String srcRelativePath = "/src/main/resources/webpage/blogs";
    private static final String targetRelativePath = "webpage/blogs";

    public static void main(String[] args) {
        File tmp = new File(BlogStorage.class.getClassLoader().
                getResource("").getPath());
        String toFilePath = tmp.getParent().replaceAll("\\\\", "/")
                + srcRelativePath;
        System.out.println(toFilePath);
    }
    /**
     * Storage the blog to local file system. The data is from the json file transferd
     * from the web page.
     * @param response  HTTP response to write
     * @param url request url
     * @param body POST method body message
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
        String blogFile = (String)paras.get("file");

        //if file is "", create a new one
        if(blogFile.equals("")) {
            writeNewBlog(paras);
        } else {
            modifyBlog(paras, blogFile);
        }

        //write back HTTP response
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "Content-Type: application/json" + "\r\n" + "\r\n";

        //returned JSON format
//        {
//            message: success/fail;
//            file: fileName
//        }
        JSONObject responseMessage = new JSONObject();
        responseMessage.put("message", "success");
        responseMessage.put("file", paras.get("file"));

        String message = head + "success!";
        HTTPLibrary.writeString(response, message.getBytes());
    }

    /**
     * This method is invoked for modify a existed blog file with json object
     * @param paras
     * @throws IOException
     */
    private static void modifyBlog(JSONObject paras, String mdName) throws IOException {
        writeFile(paras, mdName);
    }

    /**
     * This method is invoked for create a new blog file with json object
     */
    private static void writeNewBlog(JSONObject paras) throws IOException {
        String blogTitle = (String)paras.get("title");
        String mdName = generateDateName(blogTitle, ".md");
        paras.put("file", mdName);
        writeFile(paras, mdName);
    }

    /**
     * Write a new file with specified file name, delete the old one if existed
     * @param paras The content to write
     * @param mdName The markDown file name
     */
    private static void writeFile(JSONObject paras, String mdName) throws IOException {
        String blogContent = (String)paras.get("content");


        //get md file name and json file name

        String jsonName = mdName.substring(0, mdName.length() - 2) + "json";
        //create new blog markdown file
        String filePath = BlogStorage.class.getClassLoader().
                getResource(targetRelativePath).getPath();
        File mdFile = createFile(filePath + "/" + mdName);
        byte[] byteArray = blogContent.getBytes();
        //write blog content
        FileOutputStream out = new FileOutputStream(mdFile);
        out.write(byteArray);
        out.flush();
        out.close();

        //JSON Format
//        var msg = {
//                title: this.titleText.value,
//                abstract: this.abstractText.value,
//                file: this.fileName
//        }
        //write blog json file with the same name but different suffix
        File jsonFile = createFile(filePath + "/" + jsonName);
        paras.remove("content");
        byteArray = paras.toString().getBytes();
        out = new FileOutputStream(jsonFile);
        out.write(byteArray);
        out.flush();
        out.close();
        //transfer file
        transferFile(new File[]{mdFile, jsonFile});
    }

    /**
     * Create a new file with given file path, if there is an old, delete it
     * @param destFileName dst file path
     * @return File Object
     * @throws IOException
     */
    private static File createFile(String destFileName) throws IOException{
        File file = new File(destFileName);
        //if file is a directory or parent folder is not existed
        //throw exception
        if(destFileName.endsWith(File.separator) ||
                !file.getParentFile().exists()) {
            throw new IOException();
        } else {
            if(file.exists()) {
                file.delete();
            }
            //file.createNewFile();
        }
        return file;
    }

    /**
     * Transfer the file from target path to src path
     */
    private static void transferFile(File[] sourceFile) throws IOException {
        File tmp = new File(BlogStorage.class.getClassLoader().
                getResource("").getPath());
        String toFilePath = tmp.getParentFile().getParent().replaceAll("\\\\", "/")
                + srcRelativePath;
        for(File tmpFile : sourceFile) {
            File to = new File(toFilePath + "/" + tmpFile.getName());
            Files.copy(tmpFile.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

    }

    /**
     * Get a new file name base on current time
     * @return generated file name
     */
    private static String generateDateName(String title, String suffix) {
        Calendar calendar = new GregorianCalendar();
        StringBuilder name = new StringBuilder();
        name.append(calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            name.append(0);
        }
        name.append(month);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        if (date < 10) {
            name.append(0);
        }
        name.append(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            name.append(0);
        }
        name.append(hour);
        int minute = calendar.get(Calendar.MINUTE);
        if (minute < 10) {
            name.append(0);
        }
        name.append(minute);
        int second = calendar.get(Calendar.SECOND);
        if (second < 10) {
            name.append(0);
        }
        name.append(second);
        name.append(title);
        name.append(suffix);
        return name.toString();
    }
}
