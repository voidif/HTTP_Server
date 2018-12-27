package Controller.Service.JSON;

import Controller.HTTP_Server;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
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
        if(blogPath.isDirectory()) {
            JSONArray array = new JSONArray();
            File[] blogs = blogPath.listFiles();
            for(File tmp : blogs) {
                String name = tmp.getName();
                //create a JSON array for single blogs
                JSONObject blog = new JSONObject();

                blog.put("title", name);
                blog.put("url", PATH_PREFIX + name);
                array.put(blog);
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