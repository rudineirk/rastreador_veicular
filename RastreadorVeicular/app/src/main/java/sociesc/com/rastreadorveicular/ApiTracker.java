package sociesc.com.rastreadorveicular;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rudinei on 25/11/14.
 */
public class ApiTracker extends ApiClient {
    List <DataTracker> data = new ArrayList<DataTracker>();

    protected List<DataTracker> get_list(String user) {
        data.clear();
        try {
            String result = this.request(this.base_url + "/tracker/" + user + "/");
            JSONArray jsonArray =  new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trackerJson = jsonArray.getJSONObject(i);
                DataTracker tracker = new DataTracker();
                tracker.id = trackerJson.getLong("id");
                tracker.name = trackerJson.getString("name");
                tracker.user = trackerJson.getLong("user");
                tracker.serial = trackerJson.getString("serial");
                data.add(tracker);
            }
        } catch (Exception e) {
           Log.e("MainActivity", e.getMessage(), e);
        }
        return data;
    }
}
