package sociesc.com.rastreadorveicular;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rudinei on 25/11/14.
 */
abstract public class ApiClient {
    String base_url = "http://rudineirk.ddns.net:8000/api";

    protected String request(String url, String method, String body) {
        try {
            StringBuilder builder = new StringBuilder();
            Log.i("MainActivity", url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response;
            if (method == "POST") {
                HttpPost request = new HttpPost(url);
                request.setEntity(new StringEntity(body));
                response = httpClient.execute(request);
            } else if (method == "PUT") {
                HttpPut request = new HttpPut(url);
                request.setEntity(new StringEntity(body));
                response = httpClient.execute(request);
            } else if (method == "DELETE") {
                response = httpClient.execute(new HttpDelete(url));
            } else {
                response = httpClient.execute(new HttpGet(url));
            }
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
            return builder.toString();
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }
}
