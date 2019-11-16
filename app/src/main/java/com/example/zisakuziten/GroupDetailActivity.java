package com.example.zisakuziten;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class GroupDetailActivity extends AppCompatActivity {
    public Realm realm;
    public EditText titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        realm       = Realm.getDefaultInstance();
        titleText   = (EditText)findViewById(R.id.titleText);
        showData();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void showData(){
        final Group group = realm.where(Group.class).equalTo("updateTime",getIntent().getStringExtra("updateTime")).findFirst();
        titleText.setText(group.groupName);
    }



    public void create(View v){
        String title   = titleText.getText().toString();

        if (title.length() <= 2){
            Context context = getApplicationContext();
            Toast.makeText(context, "タイトルは2文字以上入力してね！", Toast.LENGTH_SHORT).show();

        }else {
            final Group group = realm.where(Group.class).equalTo("updateTime", getIntent().getStringExtra("updateTime")).findFirst();
            //realm update
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    group.groupName = titleText.getText().toString();
                }
            });
            finish();
        }
    }




}

