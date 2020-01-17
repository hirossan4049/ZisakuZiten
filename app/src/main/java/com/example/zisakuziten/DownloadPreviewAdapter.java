package com.example.zisakuziten;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import org.w3c.dom.Text;

import retrofit2.http.POST;

public class DownloadPreviewAdapter extends RecyclerView.Adapter<DownloadPreviewAdapter.ViewHolder> {
    private Group dataset;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tTextView;
        TextView cTextView;

        ViewHolder(View v){
            super(v);
            tTextView = (TextView)v.findViewById(R.id.tTextView);
            cTextView = (TextView)v.findViewById(R.id.cTextView);
        }
    }

    DownloadPreviewAdapter(Group myDataset){
        dataset = myDataset;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.download_preview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tTextView.setText(dataset.ziten_updT_List.get(position).title);
        holder.cTextView.setText(dataset.ziten_updT_List.get(position).content);

//        Log.d("DownloadPreviewAdapter",dataset[position]);
    }

    @Override
    public int getItemCount(){
        return dataset.ziten_updT_List.size();
    }


}
