package com.example.zisakuziten;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //openRealm
        realm = Realm.getDefaultInstance();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void create(View view){
        Intent intent = new Intent(this,CreateActivity.class);
        startActivity(intent);
    }
}
