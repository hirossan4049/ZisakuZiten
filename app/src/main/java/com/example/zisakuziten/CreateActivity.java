package com.example.zisakuziten;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    //from create func
    public void save(final String title,final String updateDate,final String content){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Ziten ziten = realm.createObject(Ziten.class);
                ziten.title = title;
                ziten.updateTime = updateDate;
                ziten.content = content;

                final Group group = realm.where(Group.class).equalTo("updateTime", getIntent().getStringExtra("gpupdateTime")).findFirst();
                //realm update
                group.ziten_updT_List.add(realm.where(Ziten.class).equalTo("updateTime",updateDate).findFirst());
            }

                //注意
//                ziten.groupId   = 0;
//                ziten.groupName = null;

//                ziten.probability = 0;
//                ziten.all_ans     = 0;
//                ziten.correct_ans = 0;


        });

    }


    public void create(View view){
        //titleを取得
        String title = titleText.getText().toString();
        //contentを取得
        String content = contentText.getText().toString();


        //null判定
        if(title.length() <= 1) {
            Context context = getApplicationContext();
            Toast.makeText(context, "タイトルは2文字以上入力してね！", Toast.LENGTH_SHORT).show();
        }else if(content.length() <= 1){
            Context context = getApplicationContext();
            Toast.makeText(context,"コンテンツは２文字以上入力してね!",Toast.LENGTH_SHORT).show();
        }else {

            //Date
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
            String updateDate = sdf.format(date);


//      check!
//            check(title, updateDate, content);

            //save
            save(title, updateDate, content);
            check(title,updateDate,content);

            Log.d("正常","正常にRealmに保存されました、");
            Context context = getApplicationContext();
            Toast.makeText(context, "保存成功！", Toast.LENGTH_LONG).show();
            finish();

        }
    }

    public void next(View v){
        String title   = titleText.getText().toString();
        String content = contentText.getText().toString();

        if(title.length() <= 1){
            Context context = getApplicationContext();
            Toast.makeText(context,"タイトルは２文字以上入力してね",Toast.LENGTH_SHORT).show();
        }else if(content.length() <= 1){
            Context context = getApplicationContext();
            Toast.makeText(context,"コンテンツは2文字以上入力てね",Toast.LENGTH_SHORT).show();
        }else{
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss",Locale.JAPANESE);
            String updateTime = sdf.format(date);

            save(title,updateTime,content);

            Context context = getApplicationContext();
            Toast.makeText(context,"保存成功！",Toast.LENGTH_SHORT).show();

            titleText.getEditableText().clear();
            contentText.getEditableText().clear();
        }
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




    //?
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
