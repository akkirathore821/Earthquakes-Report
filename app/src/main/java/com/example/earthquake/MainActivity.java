package com.example.earthquake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Context;
import android.content.Intent;

import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private EarthquakeAdapter adapter;
    private ListView earthquakeListView;
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);

        // Create a fake list of earthquake locations.
//        ArrayList<Earthquake> earthquakes = QueryUtils.extractFeatureFromJson();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
            // Find a reference to the {@link ListView} in the layout
            earthquakeListView = (ListView) findViewById(R.id.list);
            mProgressBar = findViewById(R.id.loading_spinner);

            // Create a new {@link ArrayAdapter} of earthquakes
            adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            earthquakeListView.setEmptyView(mEmptyStateTextView);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(adapter);

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Earthquake currentQuake = adapter.getItem(position);
                    Uri uri = Uri.parse(currentQuake.getUrl());

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });
        } else {
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_earthquakes);
        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        adapter.clear();

    }
}