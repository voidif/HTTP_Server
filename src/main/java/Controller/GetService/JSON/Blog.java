package Controller.GetService.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Get Blog Title and url JSON object
 */
public class Blog implements JSONRequest{
    private static final String PATH_PREFIX = "/blogs/";



    @Override
    public JSONObject response(JSONObject paras) {
        File blogPath = new File(
                getClass().getClassLoader().getResource("webpage/blogs").getFile());
        JSONObject result = new JSONObject();
        //find all blogs file in current directory
        //sending json format is as follow
        //var msg = {
        //    title: this.titleText.value,
        //    abstract: this.abstractText.value,
        //    url: complete url to blog file
        //}
        if(blogPath.isDirectory()) {
            JSONArray array = new JSONArray();
            File[] blogs = blogPath.listFiles();
            for(int i = 0; i < blogs.length; i = i + 2) {
                try {
                    /**
                     * for loaded JSON file format,
                     * see {@link Controller.PostService.CreateBlog#writeNewBlog(JSONObject)}
                     */
                    File tmp = blogs[i].getName().endsWith("json")? blogs[i]: blogs[i + 1];
                    FileInputStream input = new FileInputStream(tmp);
                    String jsonString = new String(input.readAllBytes());
                    //create a JSON array for single blogs
                    JSONObject blog = new JSONObject(jsonString);
                    blog.put("url", PATH_PREFIX + blog.get("file"));
                    blog.remove("file");
                    array.put(blog);
                } catch (FileNotFoundException e) {
                    System.out.println("Blog file Not found!");
                } catch (IOException e) {
                    System.out.println("Blog file existed but IO error!");
                }
            }
            result.put("blogs", array);
        }
        System.out.println(result.toString());
        return result;
    }
}


class Tester {
    public static void main(String[] args) {
        Blog blog = new Blog();
        //JSONObject json = new JSONObject();
        blog.response(null);
    }
}