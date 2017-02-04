package com.williamcomartin.plexpyremote;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.williamcomartin.plexpyremote.Adapters.HistoryAdapter;
import com.williamcomartin.plexpyremote.Helpers.Exceptions.NoServerException;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.GsonRequest;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.RequestManager;
import com.williamcomartin.plexpyremote.Models.HistoryModels;

import java.net.MalformedURLException;

public class HistoryActivity extends NavBaseActivity {

    private SharedPreferences SP;
    private RecyclerView rvHistory;
    private Context context;

    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setupActionBar();

        handleIntent(getIntent());
        
        context = this;

        rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
        loadHistory();
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        loadHistory();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private void loadHistory(){
        try {
            Uri.Builder builder = UrlHelpers.getUriBuilder();
            builder.appendQueryParameter("cmd", "get_history");
            if(!query.equals("")){
                builder.appendQueryParameter("search", query);
            }

            Log.d("LOADHISTORY", builder.toString());

            GsonRequest<HistoryModels> request = new GsonRequest<>(
                    builder.toString(),
                    HistoryModels.class,
                    null,
                    requestListener(),
                    errorListener()
            );

            RequestManager.addToRequestQueue(request);
        } catch (NoServerException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
    }

    private Response.Listener<HistoryModels> requestListener() {
        return new Response.Listener<HistoryModels>() {
            @Override
            public void onResponse(HistoryModels response) {
                HistoryAdapter adapter = new HistoryAdapter(context, response.response.data.data);
                rvHistory.setAdapter(adapter);
            }
        };
    }

    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.history);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_action_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_sort:
                Toast.makeText(this, "Sort selected", Toast.LENGTH_SHORT)
                        .show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
            return;
        }


        Intent intent = new Intent(this, ActivityActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
