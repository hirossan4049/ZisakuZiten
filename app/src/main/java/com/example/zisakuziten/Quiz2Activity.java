package com.example.zisakuziten;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Quiz2Activity extends Fragment {
    Realm realm;
    public List<Ziten> items;
    public int itemsize;
    public int answer_int;
    public Handler handler;

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
    public int sizeZero;

    public Ziten answer_realm;

    public List<Ziten> shuffle_items;
    public List<Ziten> trueList;
    public List<Ziten> falseList;

    // true == falseList_main ,false == nomal.
    public boolean now_quiz_boolean;
    //once
    public boolean random_loop;



//    public float parsent_number;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.activity_quiz_main,container,false);
        contentText     = (TextView) view.findViewById(R.id.content_text);

        handler = new Handler();

        titleText_one   = (TextView)view.findViewById(R.id.title_one);
        titleText_two   = (TextView)view.findViewById(R.id.title_two);
        titleText_three = (TextView)view.findViewById(R.id.title_three);
        titleText_four  = (TextView)view.findViewById(R.id.title_four);

//        title_zero = (TextView)view.findViewById(R.id.title_zero);

        random_loop = false;

        correct_num  = (TextView)view.findViewById(R.id.correct_num);
        all_num      = (TextView)view.findViewById(R.id.all_num);
        parsent_num  = (TextView)view.findViewById(R.id.parsent_num);
        correct_number = 0;
        all_number     = 0;
//        parsent_number = 0;

        trueList  = new ArrayList<>();
        falseList = new ArrayList<>();

        realm = Realm.getDefaultInstance();
//        RealmResults<Ziten> results = realm.where(Ziten.class).findAll();
        RealmList<Ziten> results = new RealmList<>();

//        Log.d("!!!!!!!!!",getIntent().getStringExtra("updateTime"));
        results = realm.where(Group.class).equalTo("updateTime", getArguments().getString("updateTime")).findFirst().ziten_updT_List;
        items    = realm.copyFromRealm(results);
        itemsize = items.size();
        sizeZero = 0;


        main();

        view.findViewById(R.id.one_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleText_one.getText() == answer_realm.title){
                    correct();
                }else{
                    incorrect();
                }
            }
        });
        view.findViewById(R.id.two_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleText_two.getText() == answer_realm.title){
                    correct();
                }else{
                    incorrect();
                }
            }
        });
        view.findViewById(R.id.three_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleText_three.getText() == answer_realm.title){
                    correct();
                }else{
                    incorrect();
                }
            }
        });
        view.findViewById(R.id.four_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleText_four.getText() == answer_realm.title){
                    correct();
                }else{
                    incorrect();
                }
            }
        });

    return view;
    }



    
    public void main(){
        Log.d("ITEMSIZE", String.valueOf(itemsize));
        if (itemsize <= 3){
            Log.d("NOT ITEM","OH>< ITEM NOT FOUND.");
            Toast.makeText(getContext(), "4つ以上作成してください！", Toast.LENGTH_LONG).show();
//            finish();
        }else {

            all_number++;
            //print Log.
            correct_log();
            if (all_number >= itemsize) {
                if (all_number % 3 == 0) {
                    farst_main();
                } else {
                    if (falseList.size() == 0) {
                        sizeZero ++;
                        farst_main();
                        if(sizeZero >= itemsize){
                            if(random_loop == true){farst_main();}
                            else{
                                random_loop = true;
                                Toast.makeText(getContext(), "完璧です！！(この先はランダムに出題します。）", Toast.LENGTH_LONG).show();
                                farst_main();
                            }
                        }
                    } else {
                        falseList_main();
                    }
                }
            } else {
                farst_main();
            }
        }

    }

    public void farst_main(){
        now_quiz_boolean = false;
        shuffle_items = items;

        Collections.shuffle(shuffle_items);

        answer_realm       = shuffle_items.get(0);
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

    public void falseList_main(){
        now_quiz_boolean = true;
        shuffle_items = items;

        Collections.shuffle(shuffle_items);
        Collections.shuffle(falseList);
        Integer overlapInt = shuffle_items.indexOf(falseList.get(0));

        answer_realm       = falseList.get(0);
        String content     = falseList.get(0).content;
        String answer      = falseList.get(0).title;

        //falseList(1)と重ならないように。
        Integer getItem_one;
        Integer getItem_two;
        Integer getItem_three;
        if(overlapInt == 0){
            getItem_one   = 1;
            getItem_two   = 2;
            getItem_three = 3;
        }else if(overlapInt == 1){
            getItem_one   = 0;
            getItem_two   = 2;
            getItem_three = 3;
        }else if(overlapInt == 2){
            getItem_one   = 0;
            getItem_two   = 1;
            getItem_three = 3;
        }else {
            getItem_one   = 0;
            getItem_two   = 1;
            getItem_three = 2;
        }

        String title_one = shuffle_items.get(getItem_one).title;
        String title_two = shuffle_items.get(getItem_two).title;
        String title_three = shuffle_items.get(getItem_three).title;


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
        Toast.makeText(getContext(), "正解！", Toast.LENGTH_SHORT).show();
        correct_number += 1;
        falseList.remove(answer_realm);
        trueList.add(answer_realm);
        correctOnDisplay(true);
        int parsent_number = (int) Math.floor((float) correct_number/all_number * 100);
        correct_num.setText(String.valueOf(correct_number));
        all_num.setText(String.valueOf(all_number));
        parsent_num.setText(parsent_number+"%");
        main();
    }

    public void incorrect(){
        Toast.makeText(getContext(), "不正解...", Toast.LENGTH_SHORT).show();

        trueList.remove(answer_realm);
        falseList.add(answer_realm);

        int parsent_number = (int) Math.floor((float) correct_number/all_number * 100);
        all_num.setText(String.valueOf(all_number));
        parsent_num.setText(parsent_number+"%");
        main();

    }

    public void correctOnDisplay(boolean correct){
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Quiz2Activity","correctOnDisplay Thread");
                        try {
                            buttonEnabled(false);
                            Thread.sleep(1000);
                            buttonEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("Quiz2Activity","correctOnDisplay Thread END");

                    }
                });
            }
        }).start();
    }

    public void buttonEnabled(boolean bool){
        getView().findViewById(R.id.one_btn).setEnabled(bool);
        getView().findViewById(R.id.two_btn).setEnabled(bool);
        getView().findViewById(R.id.three_btn).setEnabled(bool);
        getView().findViewById(R.id.four_btn).setEnabled(bool);
    }

    public void correct_log(){
        Log.d("correct_number",String.valueOf(correct_number));
        Log.d("all_number",String.valueOf(all_number));
        Log.d("TRUELIST", String.valueOf(trueList));
        Log.d("FALSELIST", String.valueOf(falseList));
    }



    public void next(View v){
        main();
    }





}