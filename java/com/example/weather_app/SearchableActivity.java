package com.example.weather_app;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

public class SearchableActivity extends AppCompatActivity {

    RequestQueue queue = null;
    String endpoint = "https://weather-search-hw8-257119.appspot.com/api";
    JSONObject daily;
    JSONArray dailyDataList;
    private TableLayout mTableLayout;
    DecimalFormat df = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(SearchManager.QUERY));

        //Intent i = getIntent();
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            handleSearch(getIntent().getStringExtra(SearchManager.QUERY));
        }

    }

    private void  changeButton( String param){
        if(param.equals("minus")){
            findViewById(R.id.fab_minus).setVisibility(View.GONE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.fab_minus).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.GONE);
        }

    }

    private void handleSearch(String query) {
        final String city = getIntent().getStringExtra(SearchManager.QUERY);
        queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://weather-search-hw8-257119.appspot.com/api/search";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("city", city);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                JSONObject result = new JSONObject(response.toString());
                                final String latitude = result.getString("latitude");
                                final String longitude = result.getString("longitude");


//                                final String city = result.getString("city");
//                                final String state = result.getString("region");
//                                final String fullstate = result.getString("regionName");
//                                final String countryCode = result.getString("countryCode");
                                String darkskyUrl = endpoint + "/weatherData?lat=" + latitude + "&lon=" + longitude;
                                JsonObjectRequest jsonRequest = new JsonObjectRequest
                                        (Request.Method.GET, darkskyUrl, null, new Response.Listener<JSONObject>() {
                                            @Override
                                                public void onResponse(JSONObject res) {
                                                    Log.d("My App", res.toString());

                                                    try {
                                                        setContentView(R.layout.activity_searchable);

                                                        FloatingActionButton fab = findViewById(R.id.fab);
                                                        fab.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                //SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                                                                SharedPreferences.Editor editor = pref.edit();
                                                                editor.putString(city, latitude+":"+longitude);
                                                                editor.commit();
                                                                Toast.makeText(getApplicationContext(), city+" was added to favorites!",
                                                                        Toast.LENGTH_LONG).show();
                                                                changeButton("plus");
                                                            }
                                                        });
                                                        FloatingActionButton fab_minus = findViewById(R.id.fab_minus);
                                                        fab_minus.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                //SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                                                                SharedPreferences.Editor editor = pref.edit();
                                                                editor.remove(city).commit();
                                                                editor.commit();
                                                                Toast.makeText(SearchableActivity.this, city+" was removed from favorites",
                                                                        Toast.LENGTH_LONG).show();
                                                                changeButton("minus");
                                                                Intent intent  = new Intent(SearchableActivity.this, MainActivity.class);
                                                                startActivity(intent);

                                                            }
                                                        });
                                                        JSONObject data = new JSONObject(res.toString());
                                                        final JSONObject current = data.getJSONObject("currently");
                                                        daily = data.getJSONObject("daily");
                                                        dailyDataList = daily.getJSONArray("data");
                                                        final String summary = daily.getString("summary");

                                                        double windSpeed = current.getDouble("windSpeed");
                                                        final String fWindSpeed = df.format(windSpeed);

                                                        double pressure = current.getDouble("pressure");
                                                        final String fpressure = df.format(pressure);

                                                        double precip = current.getDouble("precipIntensity");
                                                        final String fprecip = df.format(precip);

                                                        final long ftemp = Math.round(current.getDouble("temperature"));

                                                        ImageView img = findViewById(R.id.curr_icon);


                                                        String icon = current.getString("icon");
                                                        String image = "";
                                                        switch (icon) {
                                                            case "clear-day":
                                                                img.setImageResource(R.drawable.weather_sunny);
                                                                break;
                                                            case "rain":
                                                                img.setImageResource(R.drawable.weather_rainy);
                                                                break;
                                                            case "clear-night":
                                                                img.setImageResource(R.drawable.weather_night);
                                                                break;
                                                            case "sleet":
                                                                img.setImageResource(R.drawable.weather_partly_snowy_rainy_white);
                                                                break;
                                                            case "snow":
                                                                img.setImageResource(R.drawable.weather_snowy);
                                                                break;
                                                            case "wind":
                                                                img.setImageResource(R.drawable.weather_windy_variant);
                                                                break;
                                                            case "fog":
                                                                img.setImageResource(R.drawable.weather_fog_white);
                                                                break;
                                                            case "cloudy":
                                                                img.setImageResource(R.drawable.weather_cloudy);
                                                                break;
                                                            case "partly-cloudy-night":
                                                                img.setImageResource(R.drawable.weather_night_partly_cloudy);
                                                                break;
                                                            case "partly-cloudy-day":
                                                                img.setImageResource(R.drawable.weather_partly_cloudy);
                                                                break;
                                                            default:

                                                        }

                                                        final int humidity = Math.round(current.getInt("humidity") * 100);

                                                        double visibility = current.getDouble("precipIntensity");
                                                        final String fvisibility = df.format(visibility);

                                                        double cloudCover = current.getInt("cloudCover");
                                                        final long fcloudCover = Math.round(cloudCover * 100);

                                                        double ozone = current.getDouble("ozone");
                                                        final String fozone = df.format(ozone);


                                                        TextView tv = (TextView) findViewById(R.id.curr_temp);
                                                        tv.setText(ftemp + " ℉");

                                                        TextView text_summary = (TextView) findViewById(R.id.curr_summary);
                                                        String displayIconVal = icon.replace('-', ' ');
                                                        String [] arr = displayIconVal.split("\\s+");
                                                        if(arr[0].equals("partly")){
                                                            displayIconVal="";
                                                            for(int i =1; i<arr.length; i++){
                                                                displayIconVal+=arr[i]+" " ;
                                                            }

                                                        }
                                                        text_summary.setText(displayIconVal);

                                                        TextView text_city = (TextView) findViewById(R.id.curr_city);
                                                        text_city.setText(city);

                                                        ImageView info_icon = (ImageView) findViewById(R.id.info_icon);
                                                        info_icon.setImageResource(R.drawable.information_outline);

                                                        // card2 population
                                                        ImageView img_humidity = findViewById(R.id.curr_humidity);
                                                        img_humidity.setImageResource(R.drawable.water_percent);
                                                        TextView curr_humidity = (TextView) findViewById(R.id.curr_humidity_value);
                                                        curr_humidity.setText(Integer.toString(humidity) + " %");
                                                        TextView curr_humidity_txt = (TextView) findViewById(R.id.curr_humidity_text);
                                                        curr_humidity_txt.setText("Humidity");


                                                        ImageView img_windSpeed = findViewById(R.id.curr_windSpeed);
                                                        img_windSpeed.setImageResource(R.drawable.weather_windy);
                                                        TextView curr_ws = (TextView) findViewById(R.id.curr_ws_value);
                                                        curr_ws.setText(fWindSpeed + "mph");
                                                        TextView curr_ws_txt = (TextView) findViewById(R.id.curr_ws_text);
                                                        curr_ws_txt.setText("Wind Speed");

                                                        ImageView img_visibility = findViewById(R.id.curr_visibility);
                                                        img_visibility.setImageResource(R.drawable.eye_outline);
                                                        TextView curr_visibility = (TextView) findViewById(R.id.curr_visibility_value);
                                                        curr_visibility.setText(fvisibility + " km");
                                                        TextView curr_visibility_txt = (TextView) findViewById(R.id.curr_visibility_text);
                                                        curr_visibility_txt.setText("Visibility");

                                                        ImageView img_pressure = findViewById(R.id.curr_pressure);
                                                        img_pressure.setImageResource(R.drawable.gauge);
                                                        TextView curr_pressure = (TextView) findViewById(R.id.curr_pressure_value);
                                                        curr_pressure.setText(fpressure + " mb");
                                                        TextView curr_pressure_txt = (TextView) findViewById(R.id.curr_pressure_text);
                                                        curr_pressure_txt.setText("Pressure");

                                                        //card3 population
                                                        mTableLayout = findViewById(R.id.scroll_table);
                                                        mTableLayout.setStretchAllColumns(true);
                                                        mTableLayout.setBackgroundColor(Color.rgb(41, 40, 40));
                                                        mTableLayout.setPadding(10, 10,10, 10);
                                                        dailyDataList = daily.getJSONArray("data");
                                                        loadData();
                                                        final long[] lowTempArr = new long[8];
                                                        final long[] highTempArr = new long[8];
                                                        for(int t =0;t<8;t++){
                                                            final long ftempLow = Math.round(dailyDataList.getJSONObject(t).getDouble("temperatureLow"));
                                                            lowTempArr[t]=ftempLow;
                                                            final long ftempHigh = Math.round(dailyDataList.getJSONObject(t).getDouble("temperatureHigh"));
                                                            highTempArr[t]=ftempHigh;
                                                            System.out.println("Index::::"+t+ "high::"+highTempArr[t]);
                                                        }
                                                        CardView cv = findViewById(R.id.card1);
                                                        cv.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                try {
                                                                    Intent intent = new Intent(SearchableActivity.this, DetailsActivity.class);
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putCharSequence("windSpeed", fWindSpeed);
                                                                    bundle.putCharSequence("pressure", fpressure);
                                                                    bundle.putCharSequence("precipIntensity", fprecip);
                                                                    bundle.putCharSequence("temperature", Long.toString(ftemp));
                                                                    bundle.putCharSequence("icon", current.getString("icon"));
                                                                    bundle.putCharSequence("humidity", Integer.toString(humidity));
                                                                    bundle.putCharSequence("visibility", fvisibility);
                                                                    bundle.putCharSequence("cloudCover", Long.toString(fcloudCover));
                                                                    bundle.putCharSequence("ozone", fozone);
                                                                    bundle.putCharSequence("city", city);
                                                                    bundle.putCharSequence("address", city);
//                                                                    bundle.putCharSequence("state", state);
//                                                                    bundle.putCharSequence("fullstateName", fullstate);
//                                                                    bundle.putCharSequence("countryCode", countryCode);

                                                                    bundle.putLongArray("lowTempArr", lowTempArr);
                                                                    bundle.putLongArray("highTempArr", highTempArr);
                                                                    bundle.putCharSequence("summary", summary);

                                                                    intent.putExtras(bundle);

                                                                    startActivity(intent);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                }
                                            }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("Error");
                                                //TextView textView = (TextView) findViewById(R.id.grid);
                                                // textView.setText(error.toString());

                                            }
                                        });
                                queue.add(jsonRequest);

                                        }catch(Exception e){
                                e.printStackTrace();
                            }


                            }


                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error");
                        }
                    });
                queue.add(jsonObjectRequest);



        } catch (Exception e) {

        }

    }


    public void loadData() {
        try {
            for (int i = 0; i < 7; i++) {
                final TableRow tr = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                tr.setLayoutParams(lp);
                tr.setPadding(10,15,10,15);
                //tr.setBackgroundResource(R.drawable.tablerows_border);

                TextView txtView = new TextView(this);
                txtView.setGravity(Gravity.CENTER);
                String fdate = (String) android.text.format.DateFormat.format("MM/dd/yyyy ",
                        new Date(dailyDataList.getJSONObject(i).getLong("time")*1000));
                txtView.setText(fdate);
                txtView.setTextColor(Color.WHITE);
                txtView.setTextSize(20);

                ImageView iv = new ImageView(this);
                String icon = dailyDataList.getJSONObject(i).getString("icon");
                switch (icon) {
                    case "clear-day":
                        iv.setImageResource(R.drawable.weather_sunny);
                        break;
                    case "rain":
                        iv.setImageResource(R.drawable.weather_rainy);
                        break;
                    case "clear-night":
                        iv.setImageResource(R.drawable.weather_night);
                    case "sleet":
                        iv.setImageResource(R.drawable.weather_partly_snowy_rainy_white);
                    case "snow":
                        iv.setImageResource(R.drawable.weather_snowy);
                    case "wind":
                        iv.setImageResource(R.drawable.weather_windy_variant);
                    case "fog":
                        iv.setImageResource(R.drawable.weather_fog_white);
                    case "cloudy":
                        iv.setImageResource(R.drawable.weather_cloudy);
                    case "partly-cloudy-night":
                        iv.setImageResource(R.drawable.weather_night_partly_cloudy);
                    case "partly-cloudy-day":
                        iv.setImageResource(R.drawable.weather_partly_cloudy);

                }
                //iv.setImageResource(R.drawable.eye_outline);

                TextView txtView_minTemp = new TextView(this);
                txtView_minTemp.setGravity(Gravity.CENTER);
                final long ftempLow = Math.round(dailyDataList.getJSONObject(i).getDouble("temperatureLow"));
                txtView_minTemp.setText(Long.toString(ftempLow));
                txtView_minTemp.setTextColor(Color.WHITE);
                txtView_minTemp.setTextSize(20);


                TextView txtView_maxTemp = new TextView(this);
                txtView_maxTemp.setGravity(Gravity.CENTER);
                final long ftempHigh = Math.round(dailyDataList.getJSONObject(i).getDouble("temperatureHigh"));
                txtView_maxTemp.setText(Long.toString(ftempHigh));
                txtView_maxTemp.setTextColor(Color.WHITE);
                txtView_maxTemp.setTextSize(20);

                View view = new View(this);
                view.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1));
                view.setBackgroundResource(R.drawable.row_border);
                view.setBackgroundColor(Color.rgb(156, 158, 161));

                tr.addView(txtView);
                tr.addView(iv);
                tr.addView(txtView_minTemp);
                tr.addView(txtView_maxTemp);

                mTableLayout.addView(tr);
                mTableLayout.addView(view);


            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent (this, MainActivity.class);
                onBackPressed();
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
}


