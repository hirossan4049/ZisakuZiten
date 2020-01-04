package com.example.zisakuziten;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreActivity extends Fragment {
    private BottomNavigationView mBottomNav;
    public GroupService service;
    public Realm realm;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.activity_store,container,false);
        realm = Realm.getDefaultInstance();


        // くるくるするやつ。 Setup refresh listener which triggers new data loading
        //====================くるくる======================
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(),"reload",Toast.LENGTH_SHORT).show();
                setStore_item();
                swipeContainer.setRefreshing(false);
            }
        });
        // くるくるするやつの色  Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //================================================


    return view;
    }

    public void onStart() {
        super.onStart();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zisakuzitenapi2.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(GroupService.class);
        setStore_item();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }



    public void setStore_item(){
        Log.d("StoreActivity","setStore!");
        getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.c_acsess_text).setVisibility(View.GONE);
        service.getJson().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                Log.d("RESPONSE", String.valueOf(response.body()));
                if(response.isSuccessful()){
                    Log.d("response","OK");
                    setStore(response.body());
                    getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e("response","ERROR:"+t.getMessage());
                getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                getActivity().findViewById(R.id.c_acsess_text).setVisibility(View.VISIBLE);
            }
        });
    }
    public void setStore(final List<Group> groups){
        StoreAdapter storeAdapter = new StoreAdapter(getContext(),R.layout.store_item,groups);
        GridView gridView = (GridView)getActivity().findViewById(R.id.gridview);
        Collections.reverse(groups);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //item click -> preview :)
                Log.d("STOREACTIVITY","position"+ groups.get(position));
                //=================!!!!!!!!!!!!!!!====================
            }
        });
        gridView.setAdapter(storeAdapter);

    }





    public void install(final List<Group> groups){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Group groupRe = realm.createObject(Group.class);
                groupRe.groupName = groups.get(2).groupName;
                groupRe.updateTime = groups.get(2).updateTime;
                groupRe.ziten_updT_List.addAll(groups.get(2).ziten_updT_List);
            }
        });


        Group test = groups.get(0);
        Log.d("REALM ID2 GET",test.groupName+":"+test.updateTime+":"+test.ziten_updT_List.size()+"");
        Log.d("REALM","LOCAL SAVE OK!"+groups.get(2).groupName);
    }




}
