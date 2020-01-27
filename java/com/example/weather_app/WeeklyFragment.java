package com.example.weather_app;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.*;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.w3c.dom.Text;

public class WeeklyFragment extends Fragment {

    int position;
    private TextView textView;


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        WeeklyFragment weeklyFragment = new WeeklyFragment();
        weeklyFragment.setArguments(bundle);
        return weeklyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weekly_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String icon = getActivity().getIntent().getExtras().getString("icon");
        ImageView img = view.findViewById(R.id.weekly_image);
        switch(icon){
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

        TextView txtView = view.findViewById(R.id.weekly_text);
        txtView.setText(getActivity().getIntent().getExtras().getString("summary"));

        long[] lowTemp = getActivity().getIntent().getExtras().getLongArray("lowTempArr");
        long[] highTemp = getActivity().getIntent().getExtras().getLongArray("highTempArr");

        ArrayList<Entry> valueslow = new ArrayList<>();
        for(int k =0; k<8;k++){
            valueslow.add(new Entry(k, (float)lowTemp[k]));
        }

        ArrayList<Entry> valueshigh = new ArrayList<>();
        for(int j =0; j<8;j++){
            valueshigh.add(new Entry(j, (float)highTemp[j]));
        }


        LineChart mChart = view.findViewById(R.id.chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);

        LineDataSet set1 = new LineDataSet(valueslow, "Minimum Temperature");
        set1.setFillAlpha(110);
        set1.setColor(Color.rgb(204, 153, 255));
        set1.setLineWidth(1f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.rgb(204, 153, 255));

        LineDataSet set2 = new LineDataSet(valueshigh, "Maximum Temperature");
        set2.setFillAlpha(110);
        set2.setColor(Color.rgb(255, 153, 51));
        set2.setLineWidth(1f);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(Color.rgb(255, 153, 51));


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        LineData data = new LineData(dataSets);
        mChart.setData((data));
        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getAxisLeft().setTextColor(Color.WHITE);

        mChart.getLegend().setTextColor(Color.WHITE);
        mChart.getAxisLeft().setDrawGridLines(false);
        final String[] days = {"0", "1", "2", "3", "4", "5", "6", "7"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return days[(int) value];
            }
        };
        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

    }
}
