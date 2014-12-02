package sociesc.com.rastreadorveicular;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiUser extends ApiClient {
    protected DataUser login(DataUser user) {
        String response;
        DataUser response_user;
        try {
            JSONObject userJson = new JSONObject();
            userJson.accumulate("user", user.name);
            userJson.accumulate("password", user.password);
            response = this.request(
                this.base_url + "/login/",
                "POST",
                userJson.toString()
            );
            JSONObject responseJson = new JSONObject(response);
            response_user = new DataUser();
            response_user.name = responseJson.getString("name");
            response_user.active = responseJson.getBoolean("active");
            response_user.id = responseJson.getLong("id");
            return response_user;
        } catch (Exception e) {
            return null;
        }
    }
}
