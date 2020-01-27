package com.example.weather_app;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotosFragment extends Fragment implements ItemFragment.OnListFragmentInteractionListener {

    int position;
    private TextView textView;
    RequestQueue queue = null;
    private String[] imageUrls = new String[2];

    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        PhotosFragment photosFragment = new PhotosFragment();
        photosFragment.setArguments(bundle);
        return photosFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //position = getArguments().getInt("pos");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photos_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String city = getActivity().getIntent().getExtras().getString("city");
        String photosUrl = "https://weather-search-hw8-257119.appspot.com/api/customSearch?state="+city;
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, photosUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        Log.d("My App", res.toString());
                        //System.out.println("Photos::::"+res.toString());

                        try {
                            JSONObject data  = new JSONObject(res.toString());
                            String kind = data.getString("kind");
                            final JSONArray items = data.getJSONArray("items");
                            imageUrls = new String[8];
                            for(int i =0; i<8;i++){
                                imageUrls[i]= items.getJSONObject(i).getString("link");
                            }
                            if (recyclerViewAdapter == null) {
                                Fragment currentFragment = (Fragment) getChildFragmentManager().findFragmentById(R.id.main_fragment);
                                //view.findFragmentById(R.id.main_fragment);
                                //Fragment currentFragment = getActivity().getSgetSupportFragmentManager().findFragmentById(R.id.main_fragment);
                                recyclerView = (RecyclerView) currentFragment.getView();
                                recyclerViewAdapter = ((RecyclerView) currentFragment.getView()).getAdapter();
                            }
                            Runnable action = new Runnable() {
                                @Override
                                public void run() {
                                    //String[] imageUrls = getActivity().getIntent().getExtras().getStringArray("imageUrls");
                                    PictureContent.loadSavedImages(imageUrls);
                                    recyclerViewAdapter.notifyDataSetChanged();
                                }
                            };
                            getActivity().runOnUiThread(action);
                        } catch (JSONException e) {
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

    @Override
    public void onListFragmentInteraction(PictureContent.PictureItem item) {

    }

    @Override
    public void onResume() {
        super.onResume();


    }




}

