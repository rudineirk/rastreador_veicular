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
    Long usuarioId = Long.MAX_VALUE;
    String usuarioName;
    DataTracker rastreador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rastreador);

        nameRastreadorTextField = (TextView) findViewById(R.id.nomeRastreador);
        serialRastreadorTextField = (TextView) findViewById(R.id.serialRastreador);
        deleteButton = (Button) findViewById(R.id.btApagar);
        saveButton = (Button) findViewById(R.id.btSalvar);

        Intent intent = getIntent();
        rastreadorId = intent.getLongExtra("TRACKER_ID", Long.MAX_VALUE);
        usuarioId = intent.getLongExtra("USER_ID", Long.MAX_VALUE);
        usuarioName = intent.getStringExtra("USER_NAME");
        if (rastreadorId == Long.MAX_VALUE) {
            new HttpGetRastreadorTask().execute(rastreadorId);
        } else {
            this.rastreador = null;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDados();
            }
        });
    }

    private void salvarDados(){
        if(rastreadorId.equals(Long.MAX_VALUE)) {
            this.rastreador = new DataTracker();
            this.rastreador.user = usuarioId;
            this.rastreador.name = nameRastreadorTextField.getText().toString();
            this.rastreador.serial = serialRastreadorTextField.getText().toString();
            new HttpCreateRastreador().execute(this.rastreador);
        }else{
            this.rastreador.name = nameRastreadorTextField.getText().toString();
            this.rastreador.serial = serialRastreadorTextField.getText().toString();
            new HttpUpdateRastreador().execute(this.rastreador);
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

    private class HttpCreateRastreador extends AsyncTask<DataTracker, Void, Boolean > {

        @Override
        protected Boolean doInBackground(DataTracker... trackers) {
            try {
                new ApiTracker().create(trackers[0]);
                return true;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if(status) {
                showMessage("Rastreador criado com sucesso", true);
            }
        }

    }

    private class HttpUpdateRastreador extends AsyncTask<DataTracker, Void, Boolean > {

        @Override
        protected Boolean doInBackground(DataTracker... trackers) {
            try {
                new ApiTracker().update(trackers[0]);
                return true;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if(status) {
                showMessage("Rastreador salvo com sucesso", true);
            }
        }

    }

    private class HttpGetRastreadorTask extends AsyncTask<Long, Void, DataTracker > {
        DataTracker rastreador;
        @Override
        protected DataTracker doInBackground(Long... index) {
            rastreador = new ApiTracker().get_item(usuarioName, index[0].intValue());
            return rastreador;
        }

        protected void onPostExecute(DataTracker rastreador) {
            this.rastreador = rastreador;
            if(rastreador == null) {
                deleteButton.setVisibility(View.INVISIBLE);
            }else {
                nameRastreadorTextField.setText(rastreador.name);
                serialRastreadorTextField.setText(rastreador.serial);
            }
        }
    }
}
