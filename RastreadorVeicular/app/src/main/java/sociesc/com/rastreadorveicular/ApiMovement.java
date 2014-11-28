package sociesc.com.rastreadorveicular;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by rudinei on 25/11/14.
 */
public class ApiMovement extends ApiClient {
    protected DataMovement get_item(String user, String serial) {
        try {
            JSONObject movementJson = new JSONObject(this.request(
                this.base_url + "/movement/" + user + "/" + serial + "",
                "GET",
                ""
             ));
            DataMovement movement = new DataMovement();

            movement.movement = movementJson.getBoolean("movement");
            movement.pos_lat = movementJson.getDouble("pos_lat");
            movement.pos_long = movementJson.getDouble("pos_long");
            movement.pos_alt = movementJson.getDouble("pos_alt");
            movement.velocity = movementJson.getDouble("velocity");
            movement.dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(movementJson.getString("dt"));
            movement.pos_alt = movementJson.getDouble("pos_alt");

            return movement;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }
}
