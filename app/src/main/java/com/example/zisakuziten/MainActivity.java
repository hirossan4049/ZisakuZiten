package com.example.zisakuziten;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public Realm realm;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //openRealm
        realm = Realm.getDefaultInstance();
        listView = findViewById(R.id.listView);
    }



    public void setMemoList(){
        //Read Realm
        RealmResults<Ziten> results = realm.where(Ziten.class).findAll();
        List<Ziten> items = realm.copyFromRealm(results);


        ZitenAdapter adapter = new ZitenAdapter(this, R.layout.home_item, items);
        listView.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();

        setMemoList();
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
