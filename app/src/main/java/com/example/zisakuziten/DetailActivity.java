package com.example.zisakuziten;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {
    public Realm realm;
    public EditText titleText;
    public EditText contentText;
    public Button next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        realm       = Realm.getDefaultInstance();
        titleText   = (EditText)findViewById(R.id.titleText);
        contentText = (EditText)findViewById(R.id.contentText);

        next_button = (Button)findViewById(R.id.next);
        next_button.setVisibility(View.GONE);

        showData();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void showData(){
        final Ziten ziten = realm.where(Ziten.class).equalTo("updateTime",getIntent().getStringExtra("updateTime")).findFirst();

        titleText.setText(ziten.title);
        contentText.setText(ziten.content);
    }


    public void create(View v){
        String title   = titleText.getText().toString();
        String content = contentText.getText().toString();

        if (title.length() <= 2){
            Context context = getApplicationContext();
            Toast.makeText(context, "タイトルは2文字以上入力してね！", Toast.LENGTH_SHORT).show();

        }else if(content.length() <= 2){
            Context context = getApplicationContext();
            Toast.makeText(context, "内容は2文字以上入力してね！", Toast.LENGTH_SHORT).show();

        }else {

            final Ziten ziten = realm.where(Ziten.class).equalTo("updateTime", getIntent().getStringExtra("updateTime")).findFirst();
            //realm update
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ziten.title = titleText.getText().toString();
                    ziten.content = contentText.getText().toString();
                }
            });
            finish();
        }
    }

    public void next(View v) {
        Context context = getApplicationContext();
        Toast.makeText(context,"編集アクティビティでは使用できません。",Toast.LENGTH_SHORT).show();
    }



}

