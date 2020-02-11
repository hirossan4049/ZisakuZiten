package com.example.zisakuziten;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class GroupPiceActivity extends Fragment {

    public Realm realm;
    public ListView listView;
    public CheckBox checkBox;
    private BottomNavigationView mBottomNav;
    public FloatingActionButton action_button;
    public boolean checkbox_status;
    public List<List> checked_list_data;
    public List<String> checked_list;
    public List<Ziten> items;
    public ZitenAdapter adapter;
    private Vibrator mVibrator;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.activity_ziten,container,false);

        //openRealm
        realm    = Realm.getDefaultInstance();
        listView = (ListView)view.findViewById(R.id.listView);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        action_button = (FloatingActionButton)view.findViewById(R.id.action_button);
        //0 == GONE ,1 == VISIBLE
        checkbox_status = false;
        checked_list = new ArrayList<>();
        items = new ArrayList<>();

//        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        //toooooolbarrrrr
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setHasOptionsMenu(true);
        actionBar.setTitle(realm.where(Group.class).equalTo("updateTime",getArguments().getString("updateTime")).findFirst().groupName);

        //clickで編集
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkbox_status == false) {
                    Ziten ziten = (Ziten) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("updateTime", ziten.updateTime);

                    startActivity(intent);

                }else if(checkbox_status == true){
                    CheckBox checkview = view.findViewById(R.id.checkBox);

                    Ziten ziten = (Ziten)parent.getItemAtPosition(position);

                    if (checkview.isChecked() == true){
                        checkview.setChecked(false);
                        checked_list.remove(checked_list.indexOf(ziten.updateTime));
                        Log.d(String.valueOf(checked_list),"checkbox false");
                        if(checked_list.size() == 0){
                            checkbox_status = false;
                            setMemoList();
                        }

                    }else if(checkview.isChecked() == false) {
                        checkview.setChecked(true);
                        checked_list.add(ziten.updateTime);
                        Log.d(String.valueOf(checked_list),"checkbox true");
                    }
                }
            }
        });
        //Long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(100);

                checkbox_status = true;
                setMemoList();
                return false;
            }
        });



        //create button onClick
        view.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (checkbox_status == false) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.ziten_create_dialog);
                dialog.setTitle("辞典を作成");
                Button addBtn = dialog.findViewById(R.id.add);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText titleEdit = dialog.findViewById(R.id.titleEdit);
                        final EditText contentEdit = dialog.findViewById(R.id.contentEdit);
                        String title = titleEdit.getText().toString();
                        String content = contentEdit.getText().toString();
                        create(title,content);
                        setMemoList();
                        dialog.dismiss();
                    }
                });
                Button nextBtn = dialog.findViewById(R.id.next);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText titleEdit = dialog.findViewById(R.id.titleEdit);
                        final EditText contentEdit = dialog.findViewById(R.id.contentEdit);
                        String title = titleEdit.getText().toString();
                        String content = contentEdit.getText().toString();
                        create(title,content);
                        titleEdit.setText("");
                        contentEdit.setText("");
                    }
                });
                dialog.show();

            }else if(checkbox_status == true){
                delete();
            }
            }


        });

    return view;
    }

    // メニューを作成する
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Fragment fragment = new GroupActivity();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void create(final String title, final String content){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        final String updateTime = sdf.format(date);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Ziten ziten = realm.createObject(Ziten.class);
                ziten.title = title;
                ziten.content = content;
                ziten.updateTime = updateTime;

                final Group group = realm.where(Group.class)
                        .equalTo("updateTime",getArguments().getString("updateTime")).findFirst();
                group.ziten_updT_List.add(ziten);
            }
        });
    }






    public void setMemoList(){
        //Read Realm
//        List<Ziten> items = new ArrayList<>();
//        Log.d("GETSTRINGEXTRA",getIntent().getStringExtra("all"));

//        if(getIntent().getStringExtra("all_boolean") == "true"){
//            RealmResults<Ziten> gpList = realm.where(Ziten.class).findAll();
//            items = realm.copyFromRealm(gpList);
//            Log.d("EEEEEE","!!!!!!!!!!!!!!!!!!!!!!!");

//        }else {
        Bundle bundle = getArguments();
        RealmList<Ziten> gpList = realm.where(Group.class)
                .equalTo("updateTime",bundle.getString("updateTime")).findFirst().ziten_updT_List;


        items = realm.copyFromRealm(gpList);
//        }

        if (checkbox_status == false){
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_edit, null);
            action_button.setImageDrawable(drawable);
        }
        else if (checkbox_status == true){
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_delete, null);
            action_button.setImageDrawable(drawable);
        }

        adapter = new ZitenAdapter(getContext(), R.layout.home_item, items,checkbox_status);
        listView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        checkbox_status = false;
        setMemoList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }


//    public void create(View view){
//        if (checkbox_status == false) {
////            Intent intent = new Intent(getContext(), CreateActivity.class);
////            intent.putExtra("gpupdateTime",getIntent().getStringExtra("updateTime"));
////            startActivity(intent);
//        }else if(checkbox_status == true){
//            delete();
//        }
//    }

    public void delete(){
//        setMemoList();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                for(int i = 0; i < checked_list.size(); ++i) {
                    String checked_pice = checked_list.get(i);
                    Log.d("Ziten1", checked_pice);
                    Ziten realmZiten = realm.where(Ziten.class).equalTo("updateTime", checked_pice).findFirst();
//                    checked_pice.deleteFromRealm();
//                    items
                    realmZiten.deleteFromRealm();
//                    items.remove(checked_pice);
                }
                adapter.notifyDataSetChanged();
                checked_list.clear();

            }
        });
        checkbox_status = false;
        setMemoList();


    }



}
