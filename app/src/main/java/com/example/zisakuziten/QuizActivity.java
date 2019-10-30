package com.example.zisakuziten;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    public TextView contentText;
    public TextView titleText_one;
    public TextView titleText_two;
    public TextView titleText_three;
    public TextView titleText_four;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        contentText     = (TextView)findViewById(R.id.content_text);
        titleText_one   = (TextView)findViewById(R.id.titleText_one);
        titleText_two   = (TextView)findViewById(R.id.titleText_two);
        titleText_three = (TextView)findViewById(R.id.titleText_three);
        titleText_four  = (TextView)findViewById(R.id.titleText_four);

        realm = Realm.getDefaultInstance();
        RealmResults<Ziten> results = realm.where(Ziten.class).findAll();
        items    = realm.copyFromRealm(results);
        itemsize = items.size();

        main();

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


    public void titleText_one(View v){
        if (answer_int == 0){
            Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "不正解...", Toast.LENGTH_SHORT).show();

        }
    }
    public void titleText_two(View v){
        if (answer_int == 1){
            Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "不正解...", Toast.LENGTH_SHORT).show();

        }
    }

    public void titleText_three(View v){
        if (answer_int == 2){
            Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "不正解...", Toast.LENGTH_SHORT).show();

        }
    }

    public void titleText_four(View v){
        if (answer_int == 3){
            Toast.makeText(this, "正解！", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "不正解...", Toast.LENGTH_LONG).show();

        }
    }

    public void next(View v){
        main();
    }



}