package com.example.zisakuziten;

import android.app.Application;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class QuizActivity extends AppCompatActivity {
    Realm realm;
    public List<Ziten> items;
    public int itemsize;
    public int answer_int;
    private BottomNavigationView mBottomNav;

    public TextView contentText;
    public TextView titleText_one;
    public Button titleText_two;
    public Button titleText_three;
    public Button titleText_four;

    public TextView correct_num;
    public TextView all_num;
    public TextView parsent_num;
    public int correct_number;
    public int all_number;

//    public float parsent_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        contentText     = (TextView) findViewById(R.id.content_text);
        titleText_one   = (TextView) findViewById(R.id.titleText_one);
        titleText_two   = (Button) findViewById(R.id.titleText_two);
        titleText_three = (Button) findViewById(R.id.titleText_three);
        titleText_four  = (Button) findViewById(R.id.titleText_four);

        correct_num  = (TextView)findViewById(R.id.correct_num);
        all_num      = (TextView)findViewById(R.id.all_num);
        parsent_num  = (TextView)findViewById(R.id.parsent_num);
        correct_number = 0;
        all_number     = 0;
//        parsent_number = 0;

        realm = Realm.getDefaultInstance();
        RealmResults<Ziten> results = realm.where(Ziten.class).findAll();
        items    = realm.copyFromRealm(results);
        itemsize = items.size();

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
        Random random = new Random();
        int answer_size = random.nextInt(itemsize);

        String content = items.get(answer_size).content;
        String answer  = items.get(answer_size).title;

        int incorrect_one   = random.nextInt(itemsize);
        int incorrect_two   = random.nextInt(itemsize);
        int incorrect_three = random.nextInt(itemsize);

        String title_one   = items.get(incorrect_one).title;
        String title_two   = items.get(incorrect_two).title;
        String title_three = items.get(incorrect_three).title;

        contentText.setText(content);
        List<String> title_random_list = new ArrayList<String>();
        title_random_list.add(answer);
        title_random_list.add(title_one);
        title_random_list.add(title_two);
        title_random_list.add(title_three);
        Collections.shuffle(title_random_list);

        for(int i = 0; i < title_random_list.size(); i++){
            if (answer == title_random_list.get(i)){
                answer_int = i;
            }

        }
        Log.d("answer", String.valueOf(answer_int));

        titleText_one.setText(title_random_list.get(0));
        titleText_two.setText(title_random_list.get(1));
        titleText_three.setText(title_random_list.get(2));
        titleText_four.setText(title_random_list.get(3));
    }

    public void correct(){
        all_number     += 1;
        correct_number += 1;
        int parsent_number = (int) Math.floor((float) correct_number/all_number * 100);

        correct_num.setText(String.valueOf(correct_number));
        all_num.setText(String.valueOf(all_number));
        parsent_num.setText(parsent_number+"%");
    }
    public void incorrect(){
        all_number += 1;
        int parsent_number = (int) Math.floor((float) correct_number/all_number * 100);
        Log.d("correct_number",String.valueOf(correct_number));
        Log.d("all_number",String.valueOf(all_number));
        Log.d("parsent_number",String.valueOf(parsent_number));
        all_num.setText(String.valueOf(all_number));
        parsent_num.setText(parsent_number+"%");

    }


    public void titleText_one(View v){
        if (answer_int == 0){
            Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show();
            correct();
            main();
        }else{
            Toast.makeText(this, "不正解...", Toast.LENGTH_SHORT).show();
            incorrect();

        }
    }
    public void titleText_two(View v){
        if (answer_int == 1){
            Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show();
            correct();
            main();
        }else{
            Toast.makeText(this, "不正解...", Toast.LENGTH_SHORT).show();
            incorrect();

        }
    }

    public void titleText_three(View v){
        if (answer_int == 2){
            Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show();
            correct();
            main();
        }else{
            Toast.makeText(this, "不正解...", Toast.LENGTH_SHORT).show();
            incorrect();

        }
    }

    public void titleText_four(View v){
        if (answer_int == 3){
            Toast.makeText(this, "正解！", Toast.LENGTH_LONG).show();
            correct();
            main();
        }else{
            Toast.makeText(this, "不正解...", Toast.LENGTH_LONG).show();
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