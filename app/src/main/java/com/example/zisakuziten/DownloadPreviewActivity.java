package com.example.zisakuziten;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;
import java.util.List;

public class DownloadPreviewActivity extends Fragment {
//    public ListView listView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle saveIntanceState){
        View view = inflater.inflate(R.layout.activity_download,container,false);

//        listView = view.findViewById(R.id.listView);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);

        String[] list = new String[]{
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
                "hello worl;d",
        };

        RecyclerView.Adapter rAdapter = new DownloadPreviewAdapter(list);
        recyclerView.setAdapter(rAdapter);
//        setListView();
        return view;
    }


}
