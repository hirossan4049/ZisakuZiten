package com.example.zisakuziten;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public Realm realm;
    public ListView listView;
    public CheckBox checkBox;
    private BottomNavigationView mBottomNav;
    public FloatingActionButton action_button;
    public int checkbox_status;
    public List<List> checked_list_data;
    public List<Ziten> checked_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        //openRealm
        realm    = Realm.getDefaultInstance();
        listView = (ListView)findViewById(R.id.listView);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        action_button = (FloatingActionButton)findViewById(R.id.action_button);
        //0 == GONE ,1 == VISIBLE
        checkbox_status = 0;
        checked_list = new ArrayList<>();




        //clickでpiceに移動！
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Group group = (Group) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), GroupPiceActivity.class);
                    intent.putExtra("updateTime", group.updateTime);
                    startActivity(intent);
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


    // navigation view selected
    private void selectNavigation(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                checkbox_status = 0;
                setGroupList();
//                Intent intent = new Intent(this,MainActivity.class);
//                startActivity(intent);
                break;
            case R.id.quiz:
                Intent quiz_intent = new Intent(this,PlayChoiceActivity.class);
                startActivity(quiz_intent);
                break;
            case R.id.store:
                Intent store_intent = new Intent(this,StoreActivity.class);
                startActivity(store_intent);
                break;
        }

    }




    public void setGroupList(){
        //Read Realm
        RealmResults<Group> results = realm.where(Group.class).findAll();
        List<Group> items = realm.copyFromRealm(results);

        if (checkbox_status == 0){
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_edit, null);
            action_button.setImageDrawable(drawable);
        }
        else if (checkbox_status == 1){
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_delete, null);
            action_button.setImageDrawable(drawable);
        }

//        ZitenAdapter adapter = new ZitenAdapter(this, R.layout.home_item, items,checkbox_status);
        ZitenGroupAdapter adapter = new ZitenGroupAdapter(this, R.layout.group_item, items,checkbox_status);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkbox_status = 0;
        setGroupList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void create(View view){
        if (checkbox_status == 0) {
            Intent intent = new Intent(this, GroupCreateActivity.class);
            startActivity(intent);
        }else if(checkbox_status == 1){
            delete();
        }
    }

    public void delete(){
//        setGroupList();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for(int i = 0; i < checked_list.size(); ++i) {
                     Ziten checked_pice = checked_list.get(i);
                    Ziten realmZiten = realm.where(Ziten.class).equalTo("updateTime", checked_pice.updateTime).findFirst();
//                    checked_pice.deleteFromRealm();
                    realmZiten.deleteFromRealm();
                }
            }
        });


        setGroupList();


    }

    public void all(View v){
        Log.d("cliked!","all!");
    }




}
