package com.example.weather_app;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.GridView;

public class TodayFragment extends Fragment {

    int position;
    private TextView textView;
    GridView grid;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        TodayFragment todayFragment = new TodayFragment();
        todayFragment.setArguments(bundle);
        return todayFragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        position = getArguments().getInt("pos");
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.today_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String temp = getActivity().getIntent().getExtras().getString("temperature")+" ℉";


        String[] values = new String[9];
        String[] texts = new String[9];
        int[] image = new int[9];

        values[0] = getActivity().getIntent().getExtras().getString("windSpeed")+"mph";
        texts[0] = "Wind Speed";
        image[0] = R.drawable.weather_windy;
        ImageView iv_ws = view.findViewById(R.id.ws_image);
        iv_ws.setImageResource(image[0]);
        TextView valws = (TextView) view.findViewById(R.id.ws_value);
        valws.setText(values[0]);
        TextView textws = (TextView) view.findViewById(R.id.ws_text);
        textws.setText(texts[0]);

        values[1] = getActivity().getIntent().getExtras().getString("pressure")+" mb";
        texts[1] = "Pressure";
        image[1] = R.drawable.gauge;
        ImageView iv_pressure = view.findViewById(R.id.img_pressure);
        iv_pressure.setImageResource(image[1]);
        TextView valpressure = (TextView) view.findViewById(R.id.pressure_value);
        valpressure.setText(values[1]);
        TextView textpressure = (TextView) view.findViewById(R.id.pressure_text);
        textpressure.setText(texts[1]);

        values[2] = getActivity().getIntent().getExtras().getString("precipIntensity")+" mmph";
        texts[2] = "Precipitation";
        image[2]=R.drawable.weather_pouring;
        ImageView iv_precip = view.findViewById(R.id.image_precip);
        iv_precip.setImageResource(image[2]);
        TextView valprecip = (TextView) view.findViewById(R.id.precip_value);
        valprecip.setText(values[2]);
        TextView textprecip = (TextView) view.findViewById(R.id.precip_text);
        textprecip.setText(texts[2]);

        values[3] = temp;
        texts[3] = "Temperature";
        image[3]=R.drawable.thermometer;
        ImageView iv_temp = view.findViewById(R.id.image_temp);
        iv_temp.setImageResource(image[3]);
        TextView valtemp = (TextView) view.findViewById(R.id.temp_value);
        valtemp.setText(values[3]);
        TextView texttemp = (TextView) view.findViewById(R.id.temp_text);
        texttemp.setText(texts[3]);

        values[4] = getActivity().getIntent().getExtras().getString("icon");
        String icon_val = values[4];
        texts[4] = values[4];
        ImageView img = view.findViewById(R.id.image_icon);
        //iv_icon.setImageResource(image[4]);
        switch (icon_val){
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


        TextView valicon = (TextView) view.findViewById(R.id.icon_value);

        String displayIconVal = values[4].replace('-', ' ');
        String [] arr = displayIconVal.split("\\s+");
        if(arr[0].equals("partly")){
            displayIconVal="";
            for(int i =1; i<arr.length; i++){
                displayIconVal+=arr[i]+" " ;
            }

        }
        //valicon.setText(displayIconVal);
        TextView texticon = (TextView) view.findViewById(R.id.icon_text);
        texticon.setText(displayIconVal);

        values[5] = getActivity().getIntent().getExtras().getString("humidity")+" %";
        texts[5] = "Humidity";
        image[5]=R.drawable.water_percent;
        ImageView iv_humidity = view.findViewById(R.id.image_humidity);
        iv_humidity.setImageResource(image[5]);
        TextView valhumidity = (TextView) view.findViewById(R.id.humidity_value);
        valhumidity.setText(values[5]);
        TextView texthumidity = (TextView) view.findViewById(R.id.humidity_text);
        texthumidity.setText(texts[5]);

        values[6] = getActivity().getIntent().getExtras().getString("visibility")+" km";
        texts[6] = "Visibility";
        image[6]=R.drawable.eye_outline;
        ImageView iv_visibility = view.findViewById(R.id.image_visibility);
        iv_visibility.setImageResource(image[6]);
        TextView valvisi = (TextView) view.findViewById(R.id.visibility_value);
        valvisi.setText(values[6]);
        TextView textvisibility = (TextView) view.findViewById(R.id.visibility_text);
        textvisibility.setText(texts[6]);

        values[7] = getActivity().getIntent().getExtras().getString("cloudCover")+" %";
        texts[7] = "Cloud Cover";
        image[7]=R.drawable.weather_fog;
        ImageView iv_cloud = view.findViewById(R.id.image_cloud);
        iv_cloud.setImageResource(image[7]);
        TextView valcloud = (TextView) view.findViewById(R.id.cloudcover_value);
        valcloud.setText(values[7]);
        TextView textcloud = (TextView) view.findViewById(R.id.cloudcover_text);
        textcloud.setText(texts[7]);

        values[8] = getActivity().getIntent().getExtras().getString("ozone")+" DU";
        texts[8] = "Ozone";
        image[8]=R.drawable.earth;
        ImageView iv_oz = view.findViewById(R.id.image_oz);
        iv_oz.setImageResource(image[8]);
        TextView valozone = (TextView) view.findViewById(R.id.ozone_value);
        valozone.setText(values[8]);
        TextView textoz = (TextView) view.findViewById(R.id.ozone_text);
        textoz.setText(texts[0]);


    }
}