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
    protected Boolean login(DataUser user) {
        String response;
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
            response = responseJson.getString("login");
            if (response.equals("ok"))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }
}
