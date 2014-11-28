package sociesc.com.rastreadorveicular;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rudinei on 25/11/14.
 */
public class ApiLocalization extends ApiClient {
    List <DataLocalization> data = new ArrayList<DataLocalization>();

    protected List<DataLocalization> get_list(String serial) {
        data.clear();
        try {
            JSONArray jsonArray = new JSONArray(this.request(
                this.base_url + "/localization/" + serial + "/",
                "GET",
                ""
            ));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject localizationJson = jsonArray.getJSONObject(i);
                DataLocalization localization = new DataLocalization();
                localization.id = localizationJson.getLong("id");
                localization.pos_lat = localizationJson.getDouble("pos_lat");
                localization.pos_long = localizationJson.getDouble("pos_long");
                localization.pos_alt = localizationJson.getDouble("pos_alt");
                localization.velocity = localizationJson.getDouble("velocity");
                localization.dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(localizationJson.getString("dt"));
                localization.pos_alt = localizationJson.getDouble("pos_alt");
                data.add(localization);
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return data;
    }
}
