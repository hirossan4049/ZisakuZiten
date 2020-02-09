package com.example.zisakuziten;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PlayChoiceActivity extends Fragment {
    public Realm realm;
    public FloatingActionButton action_button;
    private BottomNavigationView mBottomNav;

    public Integer displayChar;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.activity_play_choice,container,false);
        realm = Realm.getDefaultInstance();
        //        action_button = (FloatingActionButton)getActivity().findViewById(R.id.action_button);
        //        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null);
        //        action_button.setImageDrawable(drawable);

//        TextView textView = (TextView)findViewById(R.id.textView);
//        if (getIntent().getStringExtra("updateTime").equals("all")){
//            textView.setText("すべて");
//        }else{
//            String group = realm.where(Group.class).equalTo("updateTime",getIntent().getStringExtra("updateTime")).findFirst().groupName;
//            textView.setText(group);
//        }
        //toooooolbarrrrr
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        setHasOptionsMenu(false);
        actionBar.setTitle("Play");

        displayChar = 0;
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
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
        view.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        return view;
    }



    public void play(){
        Context context = getContext();
//        Toast.makeText(context,"play!",Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.play_group_dialog);
        final RealmResults<Group> group = realm.where(Group.class).findAll();
        ListView listView = (ListView)dialog.findViewById(R.id.group_listView);
        List list = new ArrayList<String>();
        for(int i = 0; i < group.size();i++){
            list.add(group.get(i).groupName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(displayChar == 1){
                    dialog.dismiss();
                    Intent intent = new Intent(getContext(),QuizActivity.class);
                    startActivity(intent);

                }else if(displayChar == 2){
                    dialog.dismiss();
                    if(group.get(position).ziten_updT_List.size() < 4){
                        new AlertDialog.Builder(getActivity())
                                .setTitle(group.get(position).groupName)
                                .setMessage("単語を４つ以上追加してください！")
                                .setPositiveButton("OK", null)
                                .show();
                    }else {
                        Bundle bundle = new Bundle();
                        Fragment fragment = new Quiz2Activity();
                        bundle.putString("updateTime", group.get(position).updateTime);
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayout, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                }else if(displayChar == 3){
                    dialog.dismiss();
//                    Toast.makeText(getContext(),"comming soon...",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    Fragment fragment = new TtsActivity();
                    bundle.putString("updateTime",group.get(position).updateTime);
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout,fragment );
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    Toast.makeText(getContext(),"どれか一つを選択してください。",Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();


    }

}

