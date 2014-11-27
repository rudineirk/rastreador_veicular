package sociesc.com.rastreadorveicular;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class EditRastreador extends FragmentActivity {

    private TextView nameRastreadorTextField;
    private TextView serialRastreadorTextField;
    private Button deleteButton;
    private Button saveButton;
    Long rastreadorId = Long.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rastreador);

        nameRastreadorTextField = (TextView) findViewById(R.id.nomeRastreador);
        serialRastreadorTextField = (TextView) findViewById(R.id.serialRastreador);
        deleteButton = (Button) findViewById(R.id.btApagar);
        saveButton = (Button) findViewById(R.id.btSalvar);

        Intent intent = getIntent();
        rastreadorId = intent.getLongExtra("USER_ID", Long.MAX_VALUE);

        if(rastreadorId.equals(Long.MAX_VALUE)){
            deleteButton.setVisibility(View.INVISIBLE);
        }else{
            DataTracker rastreador = RastreadorManagerApp.getRastreadorById(rastreadorId);
            nameRastreadorTextField.setText(rastreador.name);
            serialRastreadorTextField.setText(rastreador.serial);
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDados();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apagarUsuario();
            }
        });


    }

    private void apagarUsuario(){
        new HttpRequestDeleteRastreadorTask().execute();
    }

    private void salvarDados(){
        if(rastreadorId.equals(Long.MAX_VALUE)) {
            new HttpRequestCreateRastreadorTask().execute();
        }else{
            new HttpRequestUpdateRastreadorTask().execute();
        }
    }

    private void showMessage(String message, boolean close){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if(close)
            this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_rastreador, menu);
        return true;
    }


    private class HttpRequestDeleteRastreadorTask extends AsyncTask<Void, Void, Boolean > {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                StringBuilder builder = new StringBuilder();
                String url = RastreadorManagerApp.URL_API + "/users/" + rastreadorId.toString();
                Log.i("MainActivity", url);
                HttpClient httpClient = new DefaultHttpClient();
                HttpDelete delete = new HttpDelete(url);
                delete.setHeader("Accept", "application/json");
                delete.setHeader("Content-type", "application/json");
                HttpResponse response = httpClient.execute(delete);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if(statusCode == 200)
                    return true;
                else
                    return false;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success)
                showMessage("Rastreador apagado com sucesso", true);
            else
                showMessage("Rastreador apagado com sucesso", false);
        }
    }

    private class HttpRequestUpdateRastreadorTask extends AsyncTask<Void, Void, Rastreador > {

        @Override
        protected Rastreador doInBackground(Void... params) {
            try {
                StringBuilder builder = new StringBuilder();
                String url = RastreadorManagerApp.URL_API + "/users/" + rastreadorId.toString();
                Log.i("MainActivity", url);
                HttpClient httpClient = new DefaultHttpClient();
                HttpPut put = new HttpPut(url);
                JSONObject rastreadorJson = new JSONObject();
                rastreadorJson.put("name", nameRastreadorTextField.getText().toString());
                rastreadorJson.put("email", serialRastreadorTextField.getText().toString());
                JSONObject holder = new JSONObject();
                holder.put("user", rastreadorJson);
                StringEntity se = new StringEntity(holder.toString());
                put.setEntity(se);
                put.setHeader("Accept", "application/json");
                put.setHeader("Content-type", "application/json");
                HttpResponse response = httpClient.execute(put);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                Rastreador rastreador = null;
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(builder.toString());

                    JSONObject rastreadorJsonRes = jsonObject.getJSONObject("user");
                    rastreador = new Rastreador();
                    rastreador.id = rastreadorJsonRes.getLong("id");
                    rastreador.name = rastreadorJsonRes.getString("name");
                    rastreador.serial = rastreadorJsonRes.getString("email");

                }
                return rastreador;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Rastreador rastreador) {
            if (rastreador != null) {
                showMessage("Rastreador atualizado com sucesso", true);
            }
        }
    }


    private class HttpRequestCreateRastreadorTask extends AsyncTask<Void, Void, Rastreador > {

        @Override
        protected Rastreador doInBackground(Void... params) {
            try {
                StringBuilder builder = new StringBuilder();
                String url = RastreadorManagerApp.URL_API + "/users";
                Log.i("MainActivity", url);
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                JSONObject rastreadorJson = new JSONObject();
                rastreadorJson.put("name", nameRastreadorTextField.getText().toString());
                rastreadorJson.put("email", serialRastreadorTextField.getText().toString());
                JSONObject holder = new JSONObject();
                holder.put("user", rastreadorJson);
                StringEntity se = new StringEntity(holder.toString());
                post.setEntity(se);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");
                HttpResponse response = httpClient.execute(post);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                Rastreador rastreador = null;
                if(statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(builder.toString());

                    JSONObject rastreadorJsonRes = jsonObject.getJSONObject("user");
                    rastreador = new Rastreador();
                    rastreador.id = rastreadorJsonRes.getLong("id");
                    rastreador.name = rastreadorJsonRes.getString("name");
                    rastreador.serial = rastreadorJsonRes.getString("email");

                }
                return rastreador;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Rastreador rastreador) {
            if(rastreador != null) {
                showMessage("Rastreador salvo com sucesso", true);
            }
        }

    }
}
