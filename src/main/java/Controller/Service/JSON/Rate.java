package Controller.Service.JSON;

import Controller.Service.JSON.JSONRequest;
import Model.DataBaseConnection;
import org.json.JSONObject;

public class Rate implements JSONRequest {
    public JSONObject response(JSONObject paras) {
        //get rate from database
        float rate = DataBaseConnection.getRate("USD_CNY");
        JSONObject rateJson = new JSONObject();
        rateJson.put("USD_CNY", rate);
        //response
        return rateJson;
    }
}
