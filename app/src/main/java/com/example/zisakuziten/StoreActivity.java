package com.example.zisakuziten;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNav;
    public GroupService service;
    public Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        realm = Realm.getDefaultInstance();
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zisakuzitenapi2.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(GroupService.class);


        // navigation selected, selected switch BUN is selectNavigation function
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    // navigation view selected
    private void selectNavigation(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                break;
            case R.id.quiz:
                Intent quiz_intent = new Intent(this,QuizActivity.class);
                startActivity(quiz_intent);
                break;
            case R.id.store:
                break;
        }

    }

    public void download(View v){
        jsonGet();

    }

    public void jsonGet(){
        service.getJson().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                Log.d("RESPONSE", String.valueOf(response.body()));
                if(response.isSuccessful()){
                    Log.d("response","OK");
                    install(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e("response","ERROR:"+t.getMessage());
            }
        });
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


        Group test = groups.get(2);
        Log.d("REALM ID2 GET",test.groupName+":"+test.updateTime+":"+test.ziten_updT_List.size()+"");
        Log.d("REALM","LOCAL SAVE OK!"+groups.get(2).groupName);
    }




}
