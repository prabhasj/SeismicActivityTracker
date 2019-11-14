package com.example.sidh.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sidh on 1/30/2017.
 */
public final class InformationQuery
{
    private  static long timeInMs;
    //query?eventid=us10004bgk&format=geojson\",\"felt\":0,\"cdi\":1,\"mmi\":0,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":554,\"net\":\"us\",\"code\":\"10004bgk\",\"ids\":\",us10004bgk,gcmt20160105093415,\",\"sources\":\",us,gcmt,\",\"types\":\",cap,dyfi,geoserve,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":30.75,\"rms\":0.67,\"gap\":71,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.0 - Pacific-Antarctic Ridge\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-136.2603,-54.2906,10]},\"id\":\"us10004bgk\"}],\"bbox\":[-153.4051,-54.2906,10,158.5463,59.6363,582.56]}";
    private InformationQuery()
    {

    }
    public static ArrayList<Information> extractInfo(String jsonResponse) {
        ArrayList<Information> info = new ArrayList<>();
        if(TextUtils.isEmpty(jsonResponse))
            return null;
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray features = root.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject jsonObject = features.getJSONObject(i);
                JSONObject properties = jsonObject.getJSONObject("properties");
                String url = properties.getString("url");
                double magnitude = properties.getDouble("mag");
                String place = properties.getString("place");
                timeInMs = properties.getLong("time");
                Date tim = new Date(timeInMs);
                info.add(new Information(magnitude, place, timeInMs, url));
            }
        } catch (JSONException e) {
            Log.e("InformationQuery", "Problem parsing JSON results", e);
        }
        return info;
    }


}
