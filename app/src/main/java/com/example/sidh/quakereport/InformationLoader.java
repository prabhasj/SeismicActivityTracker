package com.example.sidh.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by sidh on 2/3/2017.
 */
public class InformationLoader extends AsyncTaskLoader<ArrayList<Information>> {

    private static final String LOG_TAG = InformationLoader.class.getName();
    private String url;
    public InformationLoader(Context context,String url)
    {
        super(context);
        this.url=url;
    }
    @Override
    public ArrayList<Information> loadInBackground() {
        if(url==null)
            return null;
        URL u = createURL(url);
        String jsonResponse = maketHTTPConnection(u);
        ArrayList<Information> info = InformationQuery.extractInfo(jsonResponse);

        return info;
    }

    @Override
    protected void onStartLoading()
    {
        super.onStartLoading();
        forceLoad();
    }
    private String maketHTTPConnection(URL url) {
        String jsonResponse = "";
        HttpURLConnection connection = null;
        InputStream stream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();
            if (connection.getResponseCode()==200)
            {
                stream = connection.getInputStream();
                jsonResponse = readFromStream(stream);
            }
            else Log.e("","Error Response Code");
        } catch (IOException exception) {
            Log.e(" ", "Problem in making a connection", exception);
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException exception) {

            }
        }
        return jsonResponse;
    }



    private String readFromStream(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (stream != null) {
            InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();

    }

    private URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException exc) {
            Log.e("Problem in the url", exc.toString());
            return null;
        }
        return url;
    }
}
