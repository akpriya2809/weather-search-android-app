package com.example.weather_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class FavoriteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    int position;
    RequestQueue queue = null;
    DecimalFormat df = new DecimalFormat("#0.00");
    String endpoint = "https://weather-search-hw8-257119.appspot.com/api";
    JSONObject daily;
    JSONArray dailyDataList;
    private TableLayout mTableLayout;

    private OnFragmentInteractionListener mListener;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(int position, String fav) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putString("favorite", fav);
        FavoriteFragment favFragment = new FavoriteFragment();
        favFragment.setArguments(bundle);
        return favFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        view.findViewById(R.id.contentMain).setVisibility(View.GONE);
        //view.findViewById(R.id.tabs).setVisibility(View.GONE);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://ip-api.com/json";



        if(position!=0){
            String fav =  getArguments().getString("favorite");
            String[] favArr = fav.split(":");
            loadRequest(view, favArr);
        }else{
            view.findViewById(R.id.fab).setVisibility(View.GONE);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("My App", response.toString());

                            try {
                                JSONObject result = new JSONObject(response.toString());
                                final String latitude = result.getString("lat");
                                final String longitude = result.getString("lon");
                                final String city = result.getString("city");
                                final String state = result.getString("region");
                                final String fullstate = result.getString("regionName");
                                final String countryCode = result.getString("countryCode");



                                String darkskyUrl = endpoint + "/weatherData?lat=" + latitude + "&lon=" + longitude;
                                JsonObjectRequest jsonRequest = new JsonObjectRequest
                                        (Request.Method.GET, darkskyUrl, null, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject res) {
                                                Log.d("My App", res.toString());
                                                //spinner.setVisibility(View.GONE);
                                                //setContentView(R.layout.activity_main);

                                                try {
                                                    view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                                                    view.findViewById(R.id.contentMain).setVisibility(View.VISIBLE);
                                                    //view.findViewById(R.id.tabs).setVisibility(View.VISIBLE);
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

                                                    ImageView img = view.findViewById(R.id.curr_icon);


                                                    String icon = current.getString("icon");
                                                    String curr_summary = current.getString("summary");
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


                                                    TextView tv = (TextView) view.findViewById(R.id.curr_temp);
                                                    tv.setText(ftemp + " ℉");

                                                    TextView text_summary = (TextView) view.findViewById(R.id.curr_summary);
                                                    text_summary.setText(curr_summary);

                                                    TextView text_city = (TextView) view.findViewById(R.id.curr_city);
                                                    text_city.setText(city + ", " + state + ", " + countryCode);

                                                    ImageView info_icon = (ImageView) view.findViewById(R.id.info_icon);
                                                    info_icon.setImageResource(R.drawable.information_outline);

                                                    // card2 population
                                                    ImageView img_humidity = view.findViewById(R.id.curr_humidity);
                                                    img_humidity.setImageResource(R.drawable.water_percent);
                                                    TextView curr_humidity = (TextView) view.findViewById(R.id.curr_humidity_value);
                                                    curr_humidity.setText(Integer.toString(humidity) + " %");
                                                    TextView curr_humidity_txt = (TextView) view.findViewById(R.id.curr_humidity_text);
                                                    curr_humidity_txt.setText("Humidity");


                                                    ImageView img_windSpeed = view.findViewById(R.id.curr_windSpeed);
                                                    img_windSpeed.setImageResource(R.drawable.weather_windy);
                                                    TextView curr_ws = (TextView) view.findViewById(R.id.curr_ws_value);
                                                    curr_ws.setText(fWindSpeed + "mph");
                                                    TextView curr_ws_txt = (TextView) view.findViewById(R.id.curr_ws_text);
                                                    curr_ws_txt.setText("Wind Speed");

                                                    ImageView img_visibility = view.findViewById(R.id.curr_visibility);
                                                    img_visibility.setImageResource(R.drawable.eye_outline);
                                                    TextView curr_visibility = (TextView) view.findViewById(R.id.curr_visibility_value);
                                                    curr_visibility.setText(fvisibility + " km");
                                                    TextView curr_visibility_txt = (TextView) view.findViewById(R.id.curr_visibility_text);
                                                    curr_visibility_txt.setText("Visibility");

                                                    ImageView img_pressure = view.findViewById(R.id.curr_pressure);
                                                    img_pressure.setImageResource(R.drawable.gauge);
                                                    TextView curr_pressure = (TextView) view.findViewById(R.id.curr_pressure_value);
                                                    curr_pressure.setText(fpressure + " mb");
                                                    TextView curr_pressure_txt = (TextView) view.findViewById(R.id.curr_pressure_text);
                                                    curr_pressure_txt.setText("Pressure");

                                                    //card3 population
                                                    mTableLayout = view.findViewById(R.id.scroll_table);
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
                                                    CardView cv = view.findViewById(R.id.card1);
                                                    cv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            try {
                                                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
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
                                                                bundle.putCharSequence("state", state);
                                                                bundle.putCharSequence("fullstateName", fullstate);
                                                                bundle.putCharSequence("countryCode", countryCode);
                                                                bundle.putCharSequence("address", city+","+state+","+countryCode);
                                                                bundle.putCharSequence("latitude", latitude);
                                                                bundle.putCharSequence("longitude", longitude);
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


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            TextView textView = (TextView) view.findViewById(R.id.curr_ws_text);
//                        textView.setText("Error");
                        }
                    });
            queue.add(jsonObjectRequest);
        }




    }

    public void loadRequest(final View view, String[] favArr){
        view.findViewById(R.id.fab).setVisibility(View.VISIBLE);
        final String address = favArr[0];
        String latitude = favArr[1];
        String longitude = favArr[2];
        String darkskyUrl = endpoint + "/weatherData?lat=" + latitude + "&lon=" + longitude;
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, darkskyUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        Log.d("My App", res.toString());
                        FloatingActionButton fab = view.findViewById(R.id.fab);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

                                SharedPreferences.Editor editor = pref.edit();
                                editor.remove(address).commit();
                                editor.commit();
                                Toast.makeText(getActivity(), address+" was removed from favorites",
                                        Toast.LENGTH_LONG).show();
                                view.findViewById(R.id.fab_plus);
                                Intent intent  = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);

                            }
                        });
                        try {
                            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                            view.findViewById(R.id.contentMain).setVisibility(View.VISIBLE);
                            //view.findViewById(R.id.tabs).setVisibility(View.VISIBLE);
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

                            ImageView img = view.findViewById(R.id.curr_icon);


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
                                    img.setImageResource(R.drawable.weather_partly_snowy_rainy);
                                    break;
                                case "snow":
                                    img.setImageResource(R.drawable.weather_snowy);
                                    break;
                                case "wind":
                                    img.setImageResource(R.drawable.weather_windy);
                                    break;
                                case "fog":
                                    img.setImageResource(R.drawable.weather_fog);
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


                            TextView tv = (TextView) view.findViewById(R.id.curr_temp);
                            tv.setText(ftemp + " ℉");

                            TextView text_summary = (TextView) view.findViewById(R.id.curr_summary);
                            String displayIconVal = icon.replace('-', ' ');
                            String [] arr = displayIconVal.split("\\s+");
                            if(arr[0].equals("partly")|| arr[0].equals("Partly")){
                                displayIconVal="";
                                for(int i =1; i<arr.length; i++){
                                    displayIconVal+=arr[i]+" " ;
                                }

                            }
                            text_summary.setText(displayIconVal);

                            TextView text_city = (TextView) view.findViewById(R.id.curr_city);
                            text_city.setText(address);

                            ImageView info_icon = (ImageView) view.findViewById(R.id.info_icon);
                            info_icon.setImageResource(R.drawable.information_outline);

                            // card2 population
                            ImageView img_humidity = view.findViewById(R.id.curr_humidity);
                            img_humidity.setImageResource(R.drawable.water_percent);
                            TextView curr_humidity = (TextView) view.findViewById(R.id.curr_humidity_value);
                            curr_humidity.setText(Integer.toString(humidity) + " %");
                            TextView curr_humidity_txt = (TextView) view.findViewById(R.id.curr_humidity_text);
                            curr_humidity_txt.setText("Humidity");


                            ImageView img_windSpeed = view.findViewById(R.id.curr_windSpeed);
                            img_windSpeed.setImageResource(R.drawable.weather_windy);
                            TextView curr_ws = (TextView) view.findViewById(R.id.curr_ws_value);
                            curr_ws.setText(fWindSpeed + "mph");
                            TextView curr_ws_txt = (TextView) view.findViewById(R.id.curr_ws_text);
                            curr_ws_txt.setText("Wind Speed");

                            ImageView img_visibility = view.findViewById(R.id.curr_visibility);
                            img_visibility.setImageResource(R.drawable.eye_outline);
                            TextView curr_visibility = (TextView) view.findViewById(R.id.curr_visibility_value);
                            curr_visibility.setText(fvisibility + " km");
                            TextView curr_visibility_txt = (TextView) view.findViewById(R.id.curr_visibility_text);
                            curr_visibility_txt.setText("Visibility");

                            ImageView img_pressure = view.findViewById(R.id.curr_pressure);
                            img_pressure.setImageResource(R.drawable.gauge);
                            TextView curr_pressure = (TextView) view.findViewById(R.id.curr_pressure_value);
                            curr_pressure.setText(fpressure + " mb");
                            TextView curr_pressure_txt = (TextView) view.findViewById(R.id.curr_pressure_text);
                            curr_pressure_txt.setText("Pressure");

                            //card3 population
                            mTableLayout = view.findViewById(R.id.scroll_table);
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
                            CardView cv = view.findViewById(R.id.card1);
                            cv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
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
                                        //bundle.putCharSequence("city", city);
                                        bundle.putCharSequence("address", address);
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
    }

    public void loadData() {
        try {
            for (int i = 0; i < 7; i++) {
                final TableRow tr = new TableRow(getActivity());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                tr.setLayoutParams(lp);
                tr.setPadding(10,20,10,20);
                //tr.setBackgroundResource(R.drawable.row_border);

                TextView txtView = new TextView(getActivity());
                txtView.setGravity(Gravity.CENTER);
                String fdate = (String) android.text.format.DateFormat.format("MM/dd/yyyy ",
                        new Date(dailyDataList.getJSONObject(i).getLong("time")*1000));
                txtView.setText(fdate);
                txtView.setTextColor(Color.WHITE);
                txtView.setTextSize(20);

                ImageView iv = new ImageView(getActivity());
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
                        iv.setImageResource(R.drawable.weather_partly_snowy_rainy);
                    case "snow":
                        iv.setImageResource(R.drawable.weather_snowy);
                    case "wind":
                        iv.setImageResource(R.drawable.weather_windy);
                    case "fog":
                        iv.setImageResource(R.drawable.weather_fog);
                    case "cloudy":
                        iv.setImageResource(R.drawable.weather_cloudy);
                    case "partly-cloudy-night":
                        iv.setImageResource(R.drawable.weather_night_partly_cloudy);
                    case "partly-cloudy-day":
                        iv.setImageResource(R.drawable.weather_partly_cloudy);

                }
                //iv.setImageResource(R.drawable.eye_outline);

                TextView txtView_minTemp = new TextView(getActivity());
                txtView_minTemp.setGravity(Gravity.CENTER);
                final long ftempLow = Math.round(dailyDataList.getJSONObject(i).getDouble("temperatureLow"));
                txtView_minTemp.setText(Long.toString(ftempLow));
                txtView_minTemp.setTextColor(Color.WHITE);
                txtView_minTemp.setTextSize(20);


                TextView txtView_maxTemp = new TextView(getActivity());
                txtView_maxTemp.setGravity(Gravity.CENTER);
                final long ftempHigh = Math.round(dailyDataList.getJSONObject(i).getDouble("temperatureHigh"));
                txtView_maxTemp.setText(Long.toString(ftempHigh));
                txtView_maxTemp.setTextColor(Color.WHITE);
                txtView_maxTemp.setTextSize(20);

                View view = new View(getActivity());
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
