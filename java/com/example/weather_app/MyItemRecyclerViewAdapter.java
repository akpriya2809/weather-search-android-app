package com.example.weather_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weather_app.ItemFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;


import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<PictureContent.PictureItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private  Context context=null;

    public MyItemRecyclerViewAdapter(List<PictureContent.PictureItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Picasso.with(context).load(mValues.get(position).src).resize(1500, 1000).into(holder.imgView);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imgView;
       // public final TextView mContentView;
        public PictureContent.PictureItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imgView = (ImageView) view.findViewById(R.id.item_image_view);
            context = view.getContext();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
