import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class GetResponse {


    private InputStream request;
    private OutputStream response;

    public GetResponse(InputStream in, OutputStream out){
        request = in;
        response = out;
    }

    public void Response(){

        try {
            byte[] buffer = new byte[2048];
            int i;
            try {
                i = request.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                i = -1;
            }
            String head = new String(buffer);
//            System.out.println(head);

            //parseURL
            String url = parseURL(head);
            //System.out.println(url);
            //return index html or rate json
            if(url.length() > 1 && url.charAt(1) == '?'){
                //get rate from database
                float rate = DataBaseConnection.getRate("USD_CNY");
                JSONObject rateJson = new JSONObject();
                rateJson.put("USD_CNY", rate);
                //response
                writeJson(response, rateJson);
            } else {
                writeFile(response, url);
            }
            response.flush();
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Parse the url from the HTTP Request
     * @param requestString the HTTP Request head
     * @return URL
     * @throws MalformedURLException
     */
    public String parseURL(String requestString) throws MalformedURLException{
        String firstLine = null;
        for(int i = 0; i < requestString.length(); i ++){
            if(requestString.charAt(i) == '\r' && requestString.charAt(i + 1) == '\n'){
                firstLine = requestString.substring(0, i);
                break;
            }
        }
        if(firstLine == null){
            throw new MalformedURLException();
        }
        String url = firstLine.split(" ")[1];
        return url;
    }

    /**
     * Return the correspond file according to URL.
     * @param response The response that the file will write in.
     * @param url the url of the HTML file.
     */
    @Deprecated
    public void writeHTML(OutputStream response, String url) throws IOException{
        //read HTML
        url = "webpage" + url + "index.html";
        String htmlPath = this.getClass().getResource(url).getPath();
        File file = new File(htmlPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        //make HTTP Head
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "Content-Type: text/html; charset=utf-8" + "\r\n" + "\r\n";

        //write into response
        response.write(head.getBytes());
        response.write(data);
    }

    /**
     * Return a json object to client.
     * @param response The response that the file will write in.
     * @param json The json to be returned.
     */
    public void writeJson(OutputStream response, JSONObject json) throws IOException{
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "Content-Type: application/json" + "\r\n" + "\r\n";
        response.write(head.getBytes());
        response.write(json.toString().getBytes());
    }

    /**
     * Return a specific file to client according the URL.
     * @param response The response that the file will write in.
     * @param url The url that points to a file.
     */
    private void writeFile(OutputStream response, String url) throws IOException{
        System.out.println(url);
        if(url.equals("/")) {url = url + "index.html";}
        //read file
        url = "webpage" + url;
        String htmlPath = this.getClass().getResource(url).getPath();
        File file = new File(htmlPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        //get file extension and convert it to lower case letter
        String[] tmp = url.split("\\.");
        char[] extArray = tmp[tmp.length - 1].toCharArray();
        for(int i = 0; i < extArray.length; i ++){
            if(extArray[i] <= 'Z' && extArray[i] >= 'A'){
                extArray[i] = (char)(extArray[i] - ('A' - 'a'));
            }
        }
        String ext = new String(extArray);
        //make HTTP Head
        StringBuilder head = new StringBuilder("HTTP/1.1 200 OK" + "\r\n");
        head.append("content-length: " + data.length + "\r\n");
        //looking the file extension
        if(ext.equals("html")){
            head.append("Content-Type: text/html; charset=utf-8" + "\r\n");
        } else if(ext.equals("css")) {
            head.append("content-type: text/css; charset=utf-8" + "\r\n");
        } else if(ext.equals("jpg")) {
            head.append("Content-Type: image/png" + "\r\n");
        } else if(ext.equals("png")) {
            head.append("Content-Type: image/jpeg" + "\r\n");
        } else if(ext.equals("pdf")) {
            head.append("Content-Type: application/pdf; charset=utf-8" + "\r\n");
        }
        head.append("\r\n");
        //write into response
        response.write(head.toString().getBytes());
        response.write(data);
    }

//    public String getParasFromURL(URL url, String key){
//        String paras = url.toString().split("?")[1];
//        //TODO
//    }
}
