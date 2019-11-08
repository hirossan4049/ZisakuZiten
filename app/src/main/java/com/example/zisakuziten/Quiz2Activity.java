package com.example.zisakuziten;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class Quiz2Activity extends AppCompatActivity {
    Realm realm;
    public List<Ziten> items;
    public int itemsize;
    public int answer_int;
    private BottomNavigationView mBottomNav;

    public TextView contentText;
    public TextView titleText_one;
    public TextView titleText_two;
    public TextView titleText_three;
    public TextView titleText_four;

    public TextView correct_num;
    public TextView all_num;
    public TextView parsent_num;
    public int correct_number;
    public int all_number;

    public List<Ziten> shuffle_items;
    public List<Ziten> trueList;
    public List<Ziten> falseList;


    public TextView title_zero;

//    public float parsent_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        contentText     = (TextView) findViewById(R.id.content_text);

        titleText_one   = (TextView)findViewById(R.id.title_one);
        titleText_two   = (TextView)findViewById(R.id.title_two);
        titleText_three = (TextView)findViewById(R.id.title_three);
        titleText_four  = (TextView)findViewById(R.id.title_four);

//        title_zero = (TextView)findViewById(R.id.title_zero);

//        title_one.setText("hello world");


        correct_num  = (TextView)findViewById(R.id.correct_num);
        all_num      = (TextView)findViewById(R.id.all_num);
        parsent_num  = (TextView)findViewById(R.id.parsent_num);
        correct_number = 0;
        all_number     = 0;
//        parsent_number = 0;

        trueList  = new ArrayList<>();
        falseList = new ArrayList<>();

        realm = Realm.getDefaultInstance();
        RealmResults<Ziten> results = realm.where(Ziten.class).findAll();
        items    = realm.copyFromRealm(results);
        itemsize = items.size();
        if (itemsize < 4){
            Toast.makeText(this, "4つ以上作成してください！", Toast.LENGTH_LONG).show();
            finish();
        }

        main();

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
                finish();
                break;
            case R.id.quiz:
                break;
            case R.id.store:
                Intent store_intent = new Intent(this,StoreActivity.class);
                startActivity(store_intent);
                break;
        }

    }
    public void main(){
        all_number ++;
        if (all_number >= itemsize){

        }else{
            farst_main();
        }

    }

    public void farst_main(){
        shuffle_items = items;
        Collections.shuffle(shuffle_items);


        String content     = shuffle_items.get(0).content;
        String answer      = shuffle_items.get(0).title;
        String title_one   = shuffle_items.get(1).title;
        String title_two   = shuffle_items.get(2).title;
        String title_three = shuffle_items.get(3).title;

        String[] shuffle_settext_str = {answer,title_one,title_two,title_three};
        List<String> shuffle_setText_list = Arrays.asList(shuffle_settext_str);
        Collections.shuffle(shuffle_setText_list);

        contentText.setText(content);

        titleText_one.setText(shuffle_setText_list.get(0));
        titleText_two.setText(shuffle_setText_list.get(1));
        titleText_three.setText(shuffle_setText_list.get(2));
        titleText_four.setText(shuffle_setText_list.get(3));
    }

    public void correct(){
        Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show();

        correct_number += 1;
        falseList.remove(shuffle_items.get(0));
        trueList.add(shuffle_items.get(0));

        int parsent_number = (int) Math.floor((float) correct_number/all_number * 100);
        correct_num.setText(String.valueOf(correct_number));
        all_num.setText(String.valueOf(all_number));
        parsent_num.setText(parsent_number+"%");
        main();
    }

    public void incorrect(){
        Toast.makeText(this, "不正解...", Toast.LENGTH_SHORT).show();

        trueList.remove(shuffle_items.get(0));
        falseList.add(shuffle_items.get(0));

        int parsent_number = (int) Math.floor((float) correct_number/all_number * 100);
        all_num.setText(String.valueOf(all_number));
        parsent_num.setText(parsent_number+"%");
        main();

    }

    public void correct_log(){
        Log.d("correct_number",String.valueOf(correct_number));
        Log.d("all_number",String.valueOf(all_number));
        Log.d("TRUELIST", String.valueOf(trueList));
        Log.d("FALSELIST", String.valueOf(falseList));
    }


    public void titleText_one(View v){
        if (titleText_one.getText() == shuffle_items.get(0).title){
            correct();
        }else{
            incorrect();
        }
    }
    public void titleText_two(View v){
        if (titleText_two.getText() == shuffle_items.get(0).title){
            correct();
        }else{
            incorrect();
        }
    }
    public void titleText_three(View v){
        if (titleText_three.getText() == shuffle_items.get(0).title){
            correct();
        }else{
            incorrect();
        }
    }
    public void titleText_four(View v){
        if (titleText_four.getText() == shuffle_items.get(0).title){
            correct();
        }else{
            incorrect();
        }
    }

    public void next(View v){
        main();
    }


    public void exit(View v){
        finish();
    }

//a

}