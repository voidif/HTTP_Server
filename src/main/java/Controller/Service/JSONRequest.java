package Controller.Service;

import Model.DataBaseConnection;
import org.json.JSONObject;

/**
 * Dealing JSON related request
 */
public class JSONRequest {
}


    //get rate from database
    float rate = DataBaseConnection.getRate("USD_CNY");
    JSONObject rateJson = new JSONObject();
                rateJson.put("USD_CNY", rate);
                        //response
                        writeJson(response, rateJson);