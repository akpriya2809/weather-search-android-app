package com.example.weather_app;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;




public class FavoriteAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> title = new ArrayList<>();



    public FavoriteAdapter(FragmentManager fm, ArrayList<String> favsList) {
        super(fm);
        this.title = favsList;

    }

    @Override
    public Fragment getItem(int position) {
        return FavoriteFragment.getInstance(position, title.get(position));
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
    @Override
    public int getCount() {
        return title.size();
    }
}
