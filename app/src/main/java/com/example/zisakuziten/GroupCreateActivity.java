package com.example.zisakuziten;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;

public class GroupCreateActivity extends AppCompatActivity {
    public EditText titleText;

    public Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        //open realm
        realm = Realm.getDefaultInstance();

        titleText = (EditText) findViewById(R.id.titleText);
    }

    //app close ,realm close
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    //from create func
    public void save(final String title, final String updateTime) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Group group = realm.createObject(Group.class);
                group.groupName = title;
                group.updateTime = updateTime;
                group.ziten_updT_List = new RealmList<Ziten>();


//                ziten.probability = 0;
//                ziten.all_ans     = 0;
//                ziten.correct_ans = 0;

            }
        });

    }


    public void create(View view) {
        //titleを取得
        String title = titleText.getText().toString();


        //null判定
        if (title.length() <= 1) {
            Context context = getApplicationContext();
            Toast.makeText(context, "タイトルは2文字以上入力してね！", Toast.LENGTH_SHORT).show();
        } else {

            //Date
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
            String updateDate = sdf.format(date);

            //save
            save(title, updateDate);

            Log.d("正常", "正常にRealmに保存されました、");
            Context context = getApplicationContext();
            Toast.makeText(context, "保存成功！", Toast.LENGTH_LONG).show();
            finish();

        }
    }
}

