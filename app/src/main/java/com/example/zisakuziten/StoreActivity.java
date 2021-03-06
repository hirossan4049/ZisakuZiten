package com.example.zisakuziten;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreActivity extends Fragment {
    private BottomNavigationView mBottomNav;
    public GroupService service;
    public Realm realm;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.activity_store,container,false);
        realm = Realm.getDefaultInstance();

        // くるくるするやつ。 Setup refresh listener which triggers new data loading
        //====================くるくる======================
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(),"reload",Toast.LENGTH_SHORT).show();
                setStore_item();
                swipeContainer.setRefreshing(false);
            }
        });
        // くるくるするやつの色  Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //================================================

        //toooooolbarrrrr
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        setHasOptionsMenu(false);
        actionBar.setTitle("Store");

        view.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        //Realm Group Name selected...
                        final Group group_item = group.get(position);

                        //GroupのNameとupdateTimeをPOSTしてidをいただく
                        Call<Group> Groupcall = service.saveGPost(group_item.groupName,group_item.updateTime);
                        Groupcall.enqueue(new Callback<Group>() {
                            @Override
                            public void onResponse(Call<Group> call, Response<Group> response) {
                                Log.d("StoreActivity","upload成功(Group)"+response.body().apiId);
                                Toast.makeText(getContext(),"現在ファイルがアップロードできません。",Toast.LENGTH_LONG).show();
                                //Ziten達をPOST
//                                Call <Ziten> zitenCall = service.saveZPost()
                            }

                            @Override
                            public void onFailure(Call<Group> call, Throwable t) {
                                Log.e("StoreActivity","uploadに失敗(Group)"+call);
                            }
                        });

                        dialog.dismiss();

                    };
                });
                dialog.show();
            }//!
        });

        new AlertDialog.Builder(getActivity())
                .setTitle("Store")
                .setMessage("ストアはまだ開発途中なので利用できません。次のアップデートに期待しましょう！")
                .setPositiveButton("OK", null)
                .show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zisakuzitenapi2.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(GroupService.class);

    return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        setStore_item();
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }



    public void setStore_item(){
        //getActivityはonCreateViewの段階だとActivityが生成されてないからonActivityCreated()で。
        Log.d("StoreActivity","setStore!");
        getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.c_acsess_text).setVisibility(View.GONE);
        service.getJson().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                Log.d("RESPONSE", String.valueOf(response.body()));
                if(response.isSuccessful()){
                    Log.d("response","OK");
                    setStore(response.body());
                    getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e("response","ERROR:"+t.getMessage());
                getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                getActivity().findViewById(R.id.c_acsess_text).setVisibility(View.VISIBLE);
            }
        });
    }
    public void setStore(final List<Group> groups){
        StoreAdapter storeAdapter = new StoreAdapter(getContext(),R.layout.store_item,groups);
        GridView gridView = (GridView)getActivity().findViewById(R.id.gridview);
        Collections.reverse(groups);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //item click -> preview :)
                Log.d("STOREACTIVITY","position"+ groups.get(position));
                //=================!!!!!!!!!!!!!!!====================
                Fragment fragment = new DownloadPreviewActivity();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Group",groups.get(position));
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout,fragment );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        gridView.setAdapter(storeAdapter);

    }





    public void install(final List<Group> groups){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Group groupRe = realm.createObject(Group.class);
                groupRe.groupName = groups.get(2).groupName;
                groupRe.updateTime = groups.get(2).updateTime;
                groupRe.ziten_updT_List.addAll(groups.get(2).ziten_updT_List);
            }
        });


        Group test = groups.get(0);
        Log.d("REALM ID2 GET",test.groupName+":"+test.updateTime+":"+test.ziten_updT_List.size()+"");
        Log.d("REALM","LOCAL SAVE OK!"+groups.get(2).groupName);
    }




}
