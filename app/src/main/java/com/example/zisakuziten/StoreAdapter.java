package com.example.zisakuziten;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class StoreAdapter extends ArrayAdapter<Group> {
    private LayoutInflater layoutInflater;

    StoreAdapter(Context context,int textViewResourceId,List<Group> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Group group = getItem(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.store_item, null);
        }
        TextView gtitle = (TextView)convertView.findViewById(R.id.gtitleText);
        TextView ztitle = (TextView)convertView.findViewById(R.id.ztitleText);


        String ztitle_string;
        if (group.ziten_updT_List.size() >= 3){
            String ztitle1 = group.ziten_updT_List.get(0).title;
            String ztitle2 = group.ziten_updT_List.get(1).title;
            String ztitle3 = group.ziten_updT_List.get(2).title;
            ztitle_string = ztitle1 +" , "+ztitle2+" , "+ztitle3+"...";

        }
        //めんどくさいので3つ以上なかったらNoneとでも言っておく
        else{
            ztitle_string = "内容がないよう";
        }

//        Log.d("StoreAdapter",group.groupName);
        gtitle.setText(group.groupName);
        ztitle.setText(ztitle_string);
        return convertView;
    }

}
