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

/**
 * Created by rudinei on 25/11/14.
 */
public class ApiTracker extends ApiClient {
    List <DataTracker> data = new ArrayList<DataTracker>();

    protected List<DataTracker> get_list(String user) {
        data.clear();
        try {
            String result = this.request(
                this.base_url + "/tracker/" + user + "/",
                "GET",
                 ""
            );
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

    protected DataTracker get_item(String user, int index) {
        this.get_list(user);
        try {
            return this.data.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    protected Boolean delete(DataTracker tracker) {
        try {
            this.request(
                    this.base_url + "/tracker/" + tracker.id + "/",
                    "DELETE",
                    ""
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected Boolean create(DataTracker tracker) {
        try {
            JSONObject trackerJson = new JSONObject();
            trackerJson.accumulate("name", tracker.name);
            trackerJson.accumulate("user", tracker.user);
            trackerJson.accumulate("serial", tracker.serial);
            this.request(
                    this.base_url + "/tracker/",
                    "POST",
                    trackerJson.toString()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected Boolean update(DataTracker tracker) {
        try {
            JSONObject trackerJson = new JSONObject((Map) tracker);
            trackerJson.accumulate("id", tracker.id);
            trackerJson.accumulate("name", tracker.name);
            trackerJson.accumulate("user", tracker.user);
            trackerJson.accumulate("serial", tracker.serial);
            this.request(
                    this.base_url + "/tracker/" + tracker.id + "/",
                    "PUT",
                    trackerJson.toString()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
