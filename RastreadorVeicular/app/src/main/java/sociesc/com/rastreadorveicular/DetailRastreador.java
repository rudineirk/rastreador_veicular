package sociesc.com.rastreadorveicular;

import android.content.Intent;
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
    private GridView historicoRastreador;
    Long rastreadorId = Long.MAX_VALUE;
    Long userId = Long.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_rastreador);
        setContentView(R.layout.activity_detail_rastreador);

        nomeRastreadorTextField = (TextView) findViewById(R.id.nomeRastreador);
        estadoRastreadorTextField = (TextView) findViewById(R.id.estadoRastreador);
        ultimaVerificacaoTextField = (TextView) findViewById(R.id.ultimaVerificacao);
        velocidadeRastreadorTextField = (TextView) findViewById(R.id.velocidadeRastreador);
        serialRastreadorTextField = (TextView) findViewById(R.id.serialRastreador);
        historicoRastreador = (GridView) findViewById(R.id.gridView);
        deleteButton = (Button) findViewById(R.id.btApagar);

        Intent intent = getIntent();
        rastreadorId = intent.getLongExtra("USER_ID", Long.MAX_VALUE);

        if(rastreadorId.equals(Long.MAX_VALUE)){
            deleteButton.setVisibility(View.INVISIBLE);
        }else{
            DataTracker rastreador = RastreadorManagerApp.getRastreadorById(rastreadorId);
            nomeRastreadorTextField.setText(rastreador.name);
            serialRastreadorTextField.setText(rastreador.serial);
            new HttpGetMovement().execute(rastreador);
        }

        /*
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
        */
    }

     /*
     private void apagarUsuario(){
        new HttpRequestDeleteUserTask().execute();
    }

    private void salvarDados(){
        if(userId.equals(Long.MAX_VALUE)) {
            new HttpRequestCreateRastreadorTask().execute();
        }else{
            new HttpRequestUpdateRastreadorTask().execute();
        }
    }
    */

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
            return new ApiMovement().get_item("teste", trackers[0].serial);
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

    /*
    private class HttpRequestDeleteUserTask extends AsyncTask<Void, Void, Boolean > {
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
            return null;
        }

        @Override
        protected void onPostExecute(Rastreador rastreador) {
            if (rastreador != null) {
                showMessage("Rastreador atualizado com sucesso", true);
            }
        }
    }
    */
}
