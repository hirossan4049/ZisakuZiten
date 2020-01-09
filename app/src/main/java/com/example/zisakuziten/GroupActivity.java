package com.example.zisakuziten;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class GroupActivity extends Fragment {

    public Realm realm;
    public ListView listView;
    public CheckBox checkBox;
    private BottomNavigationView mBottomNav;
    public FloatingActionButton action_button;
    public int checkbox_status;
    public List<List> checked_list_data;
    public List<Ziten> checked_list;


//    @Override
//    protected void onCreateView(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group);
        @Override
        public View onCreateView(final LayoutInflater inflater,ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.activity_group, container, false);

            //openRealm
            realm    = Realm.getDefaultInstance();
            listView = (ListView)view.findViewById(R.id.listView);
            checkBox = (CheckBox)view.findViewById(R.id.checkBox);
//            action_button = (FloatingActionButton)view.findViewById(R.id.action_button);
            //0 == GONE ,1 == VISIBLE
            checkbox_status = 0;
            checked_list = new ArrayList<>();





            //clickでpiceに移動！
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Group group = (Group) parent.getItemAtPosition(position);
                        //!
//                        Intent intent = new Intent(getActivity().getApplicationContext(), GroupPiceActivity.class);
//                        intent.putExtra("updateTime", group.updateTime);
//                        startActivity(intent);
                        Bundle bundle = new Bundle();
                        Fragment fragment = new GroupPiceActivity();
                        bundle.putString("updateTime",group.updateTime);
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayout,fragment );
                        transaction.addToBackStack(null);
                        transaction.commit();
                }
            });
            //Group delete or hensyu
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Group group = (Group)parent.getItemAtPosition(position);
//                    ((Vibrator) getActivity(getContext().VIBRATOR_SERVICE)).vibrate(70);
                    final String[] items = {"名前変えたい！", "抹殺するんだ!"};
                    new AlertDialog.Builder(view.getContext())
                            .setTitle(group.groupName + "をどうするんや？")
                            .setItems(items, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0){
                                        Log.d("AlertDialog","item1,hensyuu");
                                        Intent intent = new Intent(getContext(),GroupDetailActivity.class);
                                        intent.putExtra("updateTime",group.updateTime);
                                        startActivity(intent);

                                    }else if(which == 1){
                                        Log.d("AlertDialog","item2,syoukyo(delete");
                                        delete(group);
                                    }else {
                                    }
                                }
                            }).show();
                    return true;
                }
            });

        // navigation selected, selected switch BUN is selectNavigation function
//        mBottomNav = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                selectNavigation(item);
//                return true;
//            }
//        });
            return view;
    }


    @Override
    public void onStart(){
        super.onStart();

        FloatingActionButton floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("actionbutton","onclick");
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.group_create_dialog);
                dialog.setTitle("hellllllo");
                Button createBtn = dialog.findViewById(R.id.create);
                createBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        final EditText editTitle = dialog.findViewById(R.id.editTitle);
                        Log.d("Dialog", String.valueOf(editTitle.getText()));
                        //Date
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
                        final String updateDate = sdf.format(date);
                        //realm save
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Group group = realm.createObject(Group.class);
                                group.groupName = editTitle.getText().toString();
                                group.updateTime = updateDate;
                                group.ziten_updT_List = new RealmList<Ziten>();
                            }
                        });
                        setGroupList();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }



    public void setGroupList(){
        //Read Realm
        RealmResults<Group> results = realm.where(Group.class).findAll();
        List<Group> items = realm.copyFromRealm(results);

//        if (checkbox_status == 0){
//            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_edit, null);
//            action_button.setImageDrawable(drawable);
//        }
//        else if (checkbox_status == 1){
//            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_delete, null);
//            action_button.setImageDrawable(drawable);
//        }

//        ZitenAdapter adapter = new ZitenAdapter(containerp.getContext(), R.layout.home_item, items,checkbox_status);
        ZitenGroupAdapter adapter = new ZitenGroupAdapter(getActivity(), R.layout.group_item, items,checkbox_status);
        listView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        checkbox_status = 0;
        setGroupList();
    }

    @Override
    //protected
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void create(View view){
        Log.d("GroupActivity","groupActivity -> groupcreateActivity");
//        Intent intent = new Intent(containerp.getContext(), GroupCreateActivity.class);
//        startActivity(intent);
//        FragmentManager fragmentManager;
//        Fragment fragment = new GroupCreateActivity();
    }


    public void delete(final Group delete_item){
//        setGroupList();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for(int i = 0; i < delete_item.ziten_updT_List.size(); ++i) {
                     Ziten checked_pice = delete_item.ziten_updT_List.get(i);
                    Ziten realmZiten = realm.where(Ziten.class).equalTo("updateTime", checked_pice.updateTime).findFirst();
                    realmZiten.deleteFromRealm();
                }
                Group delete_group = realm.where(Group.class).equalTo("updateTime",delete_item.updateTime).findFirst();
                delete_group.deleteFromRealm();
            }
        });


        setGroupList();


    }

    public void all(View v){
        Log.d("cliked!","all!");
//        Intent intent = new Intent(getApplicationContext(), GroupPiceActivity.class);
//        //booleannnnnn
//        intent.putExtra("all_boolean",true);
//        startActivity(intent);
    }


    public void onClickBtn(View v){
            Log.d("GroupActivity","onClickBtn");
    }




}
