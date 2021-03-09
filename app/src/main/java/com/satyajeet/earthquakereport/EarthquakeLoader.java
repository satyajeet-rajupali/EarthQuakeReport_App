package com.satyajeet.earthquakereport;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeDetails>> {
    private static final String LOG_TAG = EarthquakeLoader.class.getSimpleName();

    private String mUrl;

    public EarthquakeLoader(@NonNull Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<EarthquakeDetails> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        List<EarthquakeDetails> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;


    }

}
