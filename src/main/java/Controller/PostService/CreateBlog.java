package Controller.PostService;

import Controller.HTTPLibrary;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
        String blogAbstract = (String)paras.get("abstract");
        String blogContent = (String)paras.get("content");
        String blogFile = (String)paras.get("file");

        //get md file name and json file name
        String mdName = generateDateName(blogTitle, ".md");
        String jsonName = mdName.substring(0, mdName.length() - 2) + "json";
        //create new blog markdown file
        String filePath = CreateBlog.class.getClassLoader().
                getResource(targetRelativePath).getPath();
        File mdFile = createFile(filePath + "/" + mdName);
        byte[] byteArray = blogContent.getBytes();
        //write blog content
        FileOutputStream out = new FileOutputStream(mdFile);
        out.write(byteArray);
        out.flush();
        out.close();

        //write blog json file with the same name
        File jsonFile = createFile(filePath + "/" + jsonName);
        paras.remove("content");
        byteArray = paras.toString().getBytes();
        out = new FileOutputStream(jsonFile);
        out.write(byteArray);
        out.flush();
        out.close();
        //transfer file
        transferFile(new File[]{mdFile, jsonFile});


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
            //file.createNewFile();
        }
        return file;
    }

    /**
     * Transfer the file from target path to src path
     */
    private static void transferFile(File[] sourceFile) throws IOException {
        String toFilePath = CreateBlog.class.getClassLoader().
                getResource(srcRelativePath).getPath();
        for(File tmpFile : sourceFile) {
            File to = new File(toFilePath + "/" + tmpFile.getName());
            Files.copy(tmpFile.toPath(), to.toPath());
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
