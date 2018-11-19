import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


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
            //return index html or rate json
            if(url.equals("/")){
                writeHTML(response, url);
            } else if(!url.substring(0,2).equals("/f")){
                //get rate from database
                float rate = DataBaseConnection.getRate("USD_CNY");
                JSONObject rateJson = new JSONObject();
                rateJson.put("USD_CNY", rate);
                //response
                writeJson(response, rateJson);
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

//    public String getParasFromURL(URL url, String key){
//        String paras = url.toString().split("?")[1];
//        //TODO
//    }
}
