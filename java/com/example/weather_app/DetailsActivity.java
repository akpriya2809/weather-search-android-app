package com.example.weather_app;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.Map;
//import com.example.weather_app.ViewPagerAdapter;

public class DetailsActivity extends AppCompatActivity  implements ItemFragment.OnListFragmentInteractionListener{
    GridView grid;
    String city;
    String state;
    String countryCode;
    String temp;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_details);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getExtras().getString("address"));


            Bundle bundle = getIntent().getExtras();
            city = bundle.getString("city");
            state = bundle.getString("state");
            countryCode = bundle.getString("countryCode");
            temp = bundle.getString("temperature");


            viewPager = (ViewPager) findViewById(R.id.viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            adapter = new TabAdapter(getSupportFragmentManager());
            int[] tabIcons = {
                    R.drawable.calendar_today,
                    R.drawable.trending_up,
                    R.drawable.google_photos
            };
            adapter.addFragment(new TodayFragment(), "Today");
            adapter.addFragment(new WeeklyFragment(), "Weekly");
            adapter.addFragment(new PhotosFragment(), "Photos");
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);

            tabLayout.getTabAt(1).getIcon().setAlpha(128);
            tabLayout.getTabAt(2).getIcon().setAlpha(128);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            tabLayout.getTabAt(0).getIcon().setAlpha(255);
                            tabLayout.getTabAt(1).getIcon().setAlpha(128);
                            tabLayout.getTabAt(2).getIcon().setAlpha(128);
                            break;
                        case 1:
                            tabLayout.getTabAt(0).getIcon().setAlpha(128);
                            tabLayout.getTabAt(1).getIcon().setAlpha(255);
                            tabLayout.getTabAt(2).getIcon().setAlpha(128);
                            break;
                        case 2:
                            tabLayout.getTabAt(0).getIcon().setAlpha(128);
                            tabLayout.getTabAt(1).getIcon().setAlpha(128);
                            tabLayout.getTabAt(2).getIcon().setAlpha(255);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_twitter:
                String text = "Check Out "+city+", "+state+", "+countryCode+"'s Weather! It is "+temp+"\u2109 ! CSCI571 WeatherSearch";
                String url = "https://twitter.com/intent/tweet?text="+text;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onListFragmentInteraction(PictureContent.PictureItem item) {

    }
}
