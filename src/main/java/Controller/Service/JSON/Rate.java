package Controller.Service.JSON;

import Controller.Service.JSON.JSONRequest;
import Model.DataBaseConnection;
import org.json.JSONObject;

/**
 * Return USD to RMB rate JSON Object
 */
public class Rate implements JSONRequest {
    @Override
    public JSONObject response(JSONObject paras) {
        //get rate from database
        float rate = DataBaseConnection.getRate("USD_CNY");
        JSONObject rateJson = new JSONObject();
        rateJson.put("USD_CNY", rate);
        //response
        return rateJson;
    }
}
