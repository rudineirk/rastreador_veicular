package sociesc.com.rastreadorveicular;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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
import java.text.SimpleDateFormat;
import java.util.List;


public class DetailRastreador extends FragmentActivity {

    private TextView nomeRastreadorTextField;
    private TextView estadoRastreadorTextField;
    private TextView ultimaVerificacaoTextField;
    private TextView velocidadeRastreadorTextField;
    private TextView serialRastreadorTextField;
    private Button deleteButton;
    private Button showMapsButton;
    private GridView historicoRastreador;
    Long rastreadorId = Long.MAX_VALUE;
    Long userId = Long.MAX_VALUE;
    String userName;
    private Double pos_lat;
    private Double pos_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rastreador);

        nomeRastreadorTextField = (TextView) findViewById(R.id.nomeRastreador);
        estadoRastreadorTextField = (TextView) findViewById(R.id.estadoRastreador);
        ultimaVerificacaoTextField = (TextView) findViewById(R.id.ultimaVerificacao);
        velocidadeRastreadorTextField = (TextView) findViewById(R.id.velocidadeRastreador);
        serialRastreadorTextField = (TextView) findViewById(R.id.serialRastreador);
        deleteButton = (Button) findViewById(R.id.btApagar);
        showMapsButton = (Button) findViewById(R.id.gmaps);

        Intent intent = getIntent();
        rastreadorId = intent.getLongExtra("TRACKER_ID", Long.MAX_VALUE);
        userName = intent.getStringExtra("USER_NAME");

        if(rastreadorId.equals(Long.MAX_VALUE)){
            deleteButton.setVisibility(View.INVISIBLE);
        }else{
            DataTracker rastreador = RastreadorManagerApp.getRastreadorById(rastreadorId);
            nomeRastreadorTextField.setText(rastreador.name);
            serialRastreadorTextField.setText(rastreador.serial);
            new HttpGetMovement().execute(rastreador);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apagarRastreador();
            }
        });

        showMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriBegin = "geo:" + pos_lat + "," + pos_long;
                String query = pos_lat + "," + pos_long;
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

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

    private class HttpGetMovement extends AsyncTask<DataTracker, Void, DataMovement > {
        @Override
        protected DataMovement doInBackground(DataTracker... trackers) {
            return new ApiMovement().get_item(userName, trackers[0].serial);
        }

        @Override
        protected void onPostExecute(DataMovement movement) {
            if(movement != null) {
                if (movement.movement)
                    estadoRastreadorTextField.setText("Em movimento");
                else
                    estadoRastreadorTextField.setText("Parado");
                ultimaVerificacaoTextField.setText(new SimpleDateFormat().format(movement.dt));
                velocidadeRastreadorTextField.setText(movement.velocity.toString() + " km/h");
                pos_lat = movement.pos_lat;
                pos_long = movement.pos_long;
            }
        }
    }

    private class HttpGetLocalizationList extends AsyncTask<DataTracker, Void, List<DataLocalization> > {
        @Override
        protected List<DataLocalization> doInBackground(DataTracker... trackers) {
            return new ApiLocalization().get_list(trackers[0].serial);
        }

        @Override
        protected void onPostExecute(List<DataLocalization> localizations) {
            if(localizations != null) {
                for (int i=0; i < localizations.size(); i++) {
                    // Fazer a geração dos dados de
                }
            }
        }

    }

    private void apagarRastreador(){
        new HttpDeleteRastreador().execute();
    }

    private class HttpDeleteRastreador extends AsyncTask<DataTracker, Void, Boolean > {

        @Override
        protected Boolean doInBackground(DataTracker... trackers) {
            try {
                new ApiTracker().delete(trackers[0]);
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
}
