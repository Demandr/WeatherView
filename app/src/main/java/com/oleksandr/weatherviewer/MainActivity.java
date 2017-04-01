package com.oleksandr.weatherviewer;

import android.annotation.TargetApi;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final String DATE_FORMAT = "EEE,  MMM d,  yyyy";
    private static final Uri LINK = Uri.parse("http://api.openweathermap.org/data/2.5/forecast?q=Kiev")
            .buildUpon()
            .appendQueryParameter("units", "metric")
            .appendQueryParameter("APPID", "d8f1e348c20c1d685e7bfabd90eb4898")
            .build();

    private RecyclerView mRecyclerView;
    private ArrayList<Weather> mWeathers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new GetWeatherTask().execute("Khmelnytskyy", getString(R.string.localization));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(new WeatherAdapter(this, mWeathers));



        //mTextView = (TextView) findViewById(R.id.textView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String crateUrl(String city, String local){
        if (city.isEmpty()){
            Log.e(TAG, "City is empty, incorrect link");
            return null;
        }
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.web_service_url, city);

        //TODO add chose imperial or metric

        try{
            String result = baseUrl + "&units=metric" + "&lang=" + local + "&APPID=" + apiKey;
            Log.i(TAG, result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    private JSONObject getJsonObject(String urlString){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {

                    sb.append(line);
                }
                return new JSONObject(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException jse) {
            jse.printStackTrace();
        }
        finally {
            connection.disconnect();
        }
        return  null;
    }

    private void getWeather(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                Weather item = new Weather();

                //Weather item
                JSONObject json = jsonArray.getJSONObject(i);
                Log.i("JSON", jsonArray.toString());

                //Format day
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(json.getString("dt_txt").substring(0,10));

                item.setDate(DateFormat.format(DATE_FORMAT, date).toString());
                item.setTime(json.getString("dt_txt").substring(11,16));

                JSONObject jsonObjectMain = json.getJSONObject("main");

                // TODO: 01.04.2017 fix humidity
                item.setHumidity(getString(R.string.humidity, jsonObjectMain.getString("humidity")) + "%");
                item.setPressure(getString(R.string.pressure, jsonObjectMain.getString("pressure")));
                item.setTemp(getString(R.string.temp, jsonObjectMain.getString("temp")));

                //get link icon and cloudiness
                JSONArray jsonWeather = json.getJSONArray("weather");
                Log.i("JSON_WEATHER", jsonWeather.toString());
                JSONObject jsonObjectWeather = jsonWeather.getJSONObject(0);
                Log.i("JSON_OBJECT", jsonObjectWeather.toString());

                item.setCloudiness(jsonObjectWeather.getString("description"));
                item.setIcon(getString(R.string.icon_url, jsonObjectWeather.getString("icon")));
                mWeathers.add(item);
                Log.i("ICON_", item.getIcon());
            }
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private class GetWeatherTask extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject jsonObject = getJsonObject(crateUrl(urls[0], urls[1]));
            if (jsonObject == null){
                return null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                getWeather(jsonObject);
            }else Log.e("JSON", "NULL");
        }
    }
}
