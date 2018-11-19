import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

            //get rate from database
            float rate = DataBaseConnection.getRate("USD_CNY");
            JSONObject rateJson = new JSONObject();
            rateJson.put("USD_CNY", rate);
            //response
            String res = "HTTP/1.1 200 OK" + "\n\n" + rateJson.toString();

            response.write(res.getBytes());
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public URL parseURL(String requestString) throws MalformedURLException{
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
        URL url = new URL(firstLine.split(" ")[1]);
        return url;
    }

    public String getParasFromURL(URL url, String key){
        String paras = url.toString().split("?")[1];
        //TODO
    }
}
