package com.example.zisakuziten;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BottomNavigationView mBottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        Log.d("MAINACTIVITY","oncreate");

        Fragment fragment = new GroupActivity();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,fragment );
        transaction.addToBackStack(null);
        transaction.commit();

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

    // navigation view selected
    private void selectNavigation(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Log.d("Navigation","home");
                Fragment fragment = new GroupActivity();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout,fragment );
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.quiz:
                Log.d("Navigation","quiz");
                fragment = new PlayChoiceActivity();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout,fragment );
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.store:
                Log.d("Navigation","play");
                fragment = new StoreActivity();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    public void on_ziten_event(String updateTime){
        Fragment fragment = new GroupPiceActivity();

    }


}
