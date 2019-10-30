package com.example.zisakuziten;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public Realm realm;
    public ListView listView;
    public CheckBox checkBox;
    private BottomNavigationView mBottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //openRealm
        realm    = Realm.getDefaultInstance();
        listView = (ListView)findViewById(R.id.listView);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        //clickで編集
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test","test!!!!!!!!!!!!!!!!!!!!!!!!!");
                Ziten ziten = (Ziten) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                intent.putExtra("updateTime",ziten.updateTime);

                startActivity(intent);
//                switch (view.getId()) {
//                    case R.id.checkBox:
//                        break;
//                    default:
//                        startActivity(intent);
//                        break;
//                }
            }

        });

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

    // メニューを作成する
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    // メニューアイテム選択イベント（メニューが選択された時に呼ばれる）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
//                checkBox.setVisibility(View.VISIBLE);
                // ここに設定タンがタップされた時に実行する処理を追加する
                break;
            case R.id.item2:
                break;
            case R.id.item3:
                // 終了ボタンがタップされた時の処理
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    // navigation view selected
    private void selectNavigation(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.quiz:
                Intent quiz_intent = new Intent(this,QuizActivity.class);
                startActivity(quiz_intent);
                break;
            case R.id.store:
                break;
        }

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
