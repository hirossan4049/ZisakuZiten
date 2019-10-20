package com.example.zisakuziten;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {
    public Realm realm;
    public EditText titleText;
    public EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        realm = Realm.getDefaultInstance();
        titleText = (EditText)findViewById(R.id.titleText);
        contentText=(EditText)findViewById(R.id.contentText);
        showData();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void showData(){
        final Ziten ziten = realm.where(Ziten.class).equalTo("updateDate",getIntent().getStringExtra("updateDate")).findFirst();

        titleText.setText(ziten.title);
        contentText.setText(ziten.content);
    }


    public void create(View v){
        final Ziten ziten = realm.where(Ziten.class).equalTo("updateTime",getIntent().getStringExtra("updateTime")).findFirst();
        //realm update
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ziten.title   = titleText.getText().toString();
                ziten.content = contentText.getText().toString();
            }
        });
        finish();
    }



}
