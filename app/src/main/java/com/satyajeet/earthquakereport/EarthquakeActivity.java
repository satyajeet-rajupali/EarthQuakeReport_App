package com.satyajeet.earthquakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeDetails>> {

    EarthquakeDetailsAdapter mAdapter;
    private TextView emptyTextView;
    private ProgressBar progressBar;

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        progressBar = findViewById(R.id.loading_spinner);


         mAdapter= new EarthquakeDetailsAdapter(this, new ArrayList<>());

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {
            EarthquakeDetails currentEarthquake = mAdapter.getItem(position);

            Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
            startActivity(websiteIntent);
        });



        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);
        }

//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);

        emptyTextView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyTextView);


    }

    @NonNull
    @Override
    public Loader<List<EarthquakeDetails>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthquakeDetails>> loader, List<EarthquakeDetails> data) {
        progressBar.setVisibility(View.INVISIBLE);
        mAdapter.clear();

        if(data != null && !data.isEmpty()){
            mAdapter.addAll(data);
        }

        emptyTextView.setText(R.string.no_earthquakes);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthquakeDetails>> loader) {
        mAdapter.clear();
    }


//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<EarthquakeDetails>>{
//
//        @Override
//        protected List<EarthquakeDetails> doInBackground(String... strings) {
//            if(strings.length<1 || strings[0] == null){
//                return null;
//            }
//
//            ArrayList<EarthquakeDetails> result = QueryUtils.fetchEarthquakeData(strings[0]);
//            return  result;
//        }
//
//        @Override
//        protected void onPostExecute(List<EarthquakeDetails> earthquakeDetails) {
//            mAdapter.clear();
//
//            if (earthquakeDetails!=null && !earthquakeDetails.isEmpty()){
//                mAdapter.addAll(earthquakeDetails);
//            }
//        }
//    }

}