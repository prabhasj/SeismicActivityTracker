package com.example.sidh.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Information>> {

    private InformationAdapter madapter;
    private TextView textView;
    private static final int INFORMATION_LOADER_ID = 1;
    private String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //USGS_URL+=d;
        textView=(TextView) findViewById(R.id.emptyview);
        //LoaderManager manager = getLoaderManager();
        //manager.initLoader(INFORMATION_LOADER_ID, null, this);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(textView);
        madapter = new InformationAdapter(this, new ArrayList<Information>());
        listView.setAdapter(madapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Information information = madapter.getItem(position);
                Uri uri = Uri.parse(information.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting())
        {
            LoaderManager manager = getLoaderManager();
            manager.initLoader(INFORMATION_LOADER_ID, null, this);
        }
        else
        {
            View progressbar=findViewById(R.id.progressbar);
            progressbar.setVisibility(View.GONE);
            textView.setText(R.string.NoInternet);
        }

    }


    @Override
    public Loader<ArrayList<Information>> onCreateLoader(int id, Bundle args)
    {
        SharedPreferences sharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude=sharedPreference.getString(getString(R.string.setting_min_mag_key),getString(R.string.setting_min_magnitude_default));
        Log.i(" minMagnitude ",minMagnitude);
        String maxMagnitude=sharedPreference.getString(getString(R.string.setting_max_mag_key),getString(R.string.setting_max_magnitude_default));
        Log.i(" maxmMagnitude ",minMagnitude);
        String orderBy=sharedPreference.getString(getString(R.string.setting_order_by_key),getString(R.string.setting_order_by_default));
        Log.i("Order By",orderBy);
        String limit=sharedPreference.getString(getString(R.string.setting_limit_key),getString(R.string.setting_limit_default));
        Log.i("Limit",limit);
        String start=sharedPreference.getString(getString(R.string.setting_start_time_key),getString(R.string.setting_start_time_default));
        SimpleDateFormat format=new SimpleDateFormat("yyyy-dd-MM",new Locale("en","in"));
        Date date=new Date();
        String d=format.format(date);
        Log.i(d,date.toString());
        String end=sharedPreference.getString(getString(R.string.setting_end_time_key),d);
        Log.i("start",start);
        Log.i("end",end);
        Uri baseUri=Uri.parse(USGS_URL);
        Uri.Builder uriBuilder=baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("starttime",start);
        uriBuilder.appendQueryParameter("endtime",end);
        uriBuilder.appendQueryParameter("limit",limit);
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("maxmag",maxMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);
        //uriBuilder.appendQueryParameter("orderby",orderBy);
        Log.i("URL", uriBuilder.toString());
        return new InformationLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Information>> loader,ArrayList<Information> info)
    {
       textView.setText("Earthquake Data Not Found ");
        View progressbar=findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);
        madapter.clear();
        if(info!=null)
        {
            madapter.addAll(info);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Information>> loader) {
        madapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}