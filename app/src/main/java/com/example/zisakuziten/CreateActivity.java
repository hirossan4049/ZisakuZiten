package com.example.zisakuziten;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import io.realm.Realm;

public class CreateActivity extends AppCompatActivity {
    public EditText titleText;
    public EditText contentText;

    public Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //open realm
        realm = Realm.getDefaultInstance();



        titleText   = (EditText)findViewById(R.id.titleText);
        contentText = (EditText)findViewById(R.id.contentText);
    }

    //app close ,realm close
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    //from add func
    public void save(final String title,final String updateDate,final String content){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Ziten ziten = realm.createObject(Ziten.class);
                ziten.title      = title;
                ziten.updateTime = updateDate;
                ziten.content    = content;
            }
        });

    }


    public void create(View view){
        //titleを取得
        String title = titleText.getText().toString();

        //Date
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        String updateDate = sdf.format(date);

        //contentを取得
        String content = contentText.getText().toString();
//      check!
        check(title,updateDate,content);

        //save
        save(title,updateDate,content);
        finish();


    }




    public void check(String title,String updateDate,String content){
        Ziten ziten = new Ziten();

        ziten.title      = title;
        ziten.updateTime = updateDate;
        ziten.content    = content;

        Log.d("Ziten_title",ziten.title);
        Log.d("Ziten_update",ziten.updateTime);
        Log.d("Ziten_content",ziten.content);

    }



    public void add(View v){
        //title and content を取得
        String title = titleText.getText().toString();
        Log.d("title : " ,title);
        String content = contentText.getText().toString();
        Log.d("content : " ,content);

        //date
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        String updateTime = sdf.format(date);



        //save func
        save(title,updateTime,content);
    }
}
