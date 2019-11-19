package com.example.zisakuziten;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
        //Group sakuzyo or hensyu
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Group group = (Group)parent.getItemAtPosition(position);
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
                final String[] items = {"名前変えたい！", "抹殺するんだ!"};
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(group.groupName + "をどうするんや？")
                        .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    Log.d("AlertDialog","item1,hensyuu");
                                    Intent intent = new Intent(getApplicationContext(),GroupDetailActivity.class);
                                    intent.putExtra("updateTime",group.updateTime);
                                    startActivity(intent);

                                }else if(which == 1){
                                    Log.d("AlertDialog","item2,syoukyo(delete");
                                    delete(group);
                                }else {
                                }
                            }
                        }).show();
                return true;
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
                                            //group
                quiz_intent.putExtra("updateTime","all");
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
        Intent intent = new Intent(this, GroupCreateActivity.class);
        startActivity(intent);
    }


    public void delete(final Group delete_item){
//        setGroupList();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for(int i = 0; i < delete_item.ziten_updT_List.size(); ++i) {
                     Ziten checked_pice = delete_item.ziten_updT_List.get(i);
                    Ziten realmZiten = realm.where(Ziten.class).equalTo("updateTime", checked_pice.updateTime).findFirst();
                    realmZiten.deleteFromRealm();
                }
                Group delete_group = realm.where(Group.class).equalTo("updateTime",delete_item.updateTime).findFirst();
                delete_group.deleteFromRealm();
            }
        });


        setGroupList();


    }

    public void all(View v){
        Log.d("cliked!","all!");
//        Intent intent = new Intent(getApplicationContext(), GroupPiceActivity.class);
//        //booleannnnnn
//        intent.putExtra("all_boolean",true);
//        startActivity(intent);
    }




}
