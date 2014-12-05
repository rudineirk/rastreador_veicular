package sociesc.com.rastreadorveicular;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity
        implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ImageButton fab;
    private DataUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user = new DataUser();
        user.active = intent.getBooleanExtra("USER_ACTIVE", false);
        user.id = intent.getLongExtra("USER_ID", Long.MAX_VALUE);
        user.name = intent.getStringExtra("USER_NAME");

        fab = (ImageButton) findViewById(R.id.fab_add);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new RastreadorAdapter(RastreadorManagerApp.getRastreadores());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(

                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override public void onItemClick(View view, int position) {
                        Log.i(MainActivity.class.getName(),"LISTA CLICADA " + position);
                        DataTracker rastreador = RastreadorManagerApp.getRastreadorByPosition(position);
                        openDetailRastreador(rastreador.id);
                    }
                })
        );

        fab.setOnClickListener(this);
    }

    private void refreshData(){
        new HttpRequestRastreadoresTask().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            System.out.println("BOTAO ADD CLICADO");
            openEditRastreador(Long.MAX_VALUE);
        }
        else if (view.getId() == R.id.container_list_item) {
            System.out.println("LISTA CLICADA");
        }
    }

    private void openEditRastreador(Long rastreadorId){
        Intent intent = new Intent(this, EditRastreador.class);
        intent.putExtra("TRACKER_ID", rastreadorId);
        intent.putExtra("USER_NAME", user.name);
        intent.putExtra("USER_ID", user.id);
        startActivity(intent);
    }

    private void openDetailRastreador(Long rastreadorId){
        Intent intent = new Intent(this, DetailRastreador.class);
        intent.putExtra("TRACKER_ID", rastreadorId);
        intent.putExtra("USER_NAME", user.name);
        intent.putExtra("USER_ID", user.id);
        startActivity(intent);
    }


    private class HttpRequestRastreadoresTask extends AsyncTask<Void, Void, List<DataTracker> > {

        List<DataTracker> rastreadores;
        @Override
        protected List<DataTracker> doInBackground(Void... params) {
            rastreadores = new ApiTracker().get_list(user.name);
            return rastreadores;
        }

        protected void onPostExecute(List<DataTracker> rastreadores) {
            RastreadorManagerApp.setRastreadores(rastreadores);
            mAdapter = new RastreadorAdapter(rastreadores);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
