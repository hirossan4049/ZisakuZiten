package com.example.zisakuziten;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.Person;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class DownloadPreviewActivity extends Fragment {
//    public ListView listView;
    public Realm realm;
    public Button download_btn;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle saveIntanceState){
        View view = inflater.inflate(R.layout.activity_download,container,false);
        realm = Realm.getDefaultInstance();
        download_btn = view.findViewById(R.id.download_btn);
//        listView = view.findViewById(R.id.listView);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);


        Group group = (Group) getArguments().getSerializable("Group");
        download_btn.setText(group.groupName + " をダウンロード");

        RecyclerView.Adapter rAdapter = new DownloadPreviewAdapter(group);
        recyclerView.setAdapter(rAdapter);
//        setListView();

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();
                final Group group = (Group) bundle.getSerializable("Group");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Group groupRe = realm.createObject(Group.class);
                        groupRe.groupName = group.groupName;
                        groupRe.updateTime = group.updateTime;
                        groupRe.ziten_updT_List.addAll(group.ziten_updT_List);
                    }
                });
                Log.d("DownloadPreviewActivity", String.valueOf(group.ziten_updT_List.size()));
                Toast.makeText(getContext(),"ダウンロードしたよ！",Toast.LENGTH_LONG).show();


//                Group test = groups.get(0);
//                Log.d("REALM ID2 GET",test.groupName+":"+test.updateTime+":"+test.ziten_updT_List.size()+"");
//                Log.d("REALM","LOCAL SAVE OK!"+groups.get(2).groupName);
            }

        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
