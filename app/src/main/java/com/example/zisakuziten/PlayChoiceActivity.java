package com.example.zisakuziten;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlayChoiceActivity extends AppCompatActivity {
    public FloatingActionButton action_button;
    private BottomNavigationView mBottomNav;

    public Integer displayChar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_choice);


        action_button = (FloatingActionButton)findViewById(R.id.action_button);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null);
        action_button.setImageDrawable(drawable);

        mBottomNav = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });

        displayChar = 0;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.radioButton1:
                        displayChar = 1;
                        break;
                    case R.id.radioButton2:
                        displayChar = 2;
                        break;
                    case R.id.radioButton3:
                        displayChar = 3;
                }
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


    public void play(View v){
        Context context = getApplicationContext();
//        Toast.makeText(context,"play!",Toast.LENGTH_SHORT).show();

        if(displayChar == 1){
            Intent intent = new Intent(this,QuizActivity.class);
            startActivity(intent);

        }else if(displayChar == 2){
            Intent intent = new Intent(this,Quiz2Activity.class);
            startActivity(intent);

        }else if(displayChar == 3){

        }else{
            Toast.makeText(context,"どれか一つを選択してください。",Toast.LENGTH_SHORT).show();
        }

    }

}

