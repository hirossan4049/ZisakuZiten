package com.example.zisakuziten;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ZitenGroupAdapter extends ArrayAdapter<Group> {

    private LayoutInflater layoutinflater;
    //    public CheckBox checkBox;
    public int checkbox;


    ZitenGroupAdapter(Context context, int textViewResourceId, List<Group> objects,int checkbox_status) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkbox       = checkbox_status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Group group = getItem(position);

        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.group_item, null);
        }

        TextView titleText   = (TextView) convertView.findViewById(R.id.titleText);

        titleText.setText(String.valueOf(group.groupName));

//        contentText.setText(String.valueOf(group.groupId));

        return convertView;
    }

//    public void checkbox_visible(){
//        checkBox.setVisibility(View.VISIBLE);
//    }

//    public class checkbox_chenge{
//        public CheckBox checkBox;
//
//        public void setGone(){
//            checkBox.setVisibility(View.GONE);
//        }
//        public void setVISIBLE(){
//            checkBox.setVisibility(View.VISIBLE);
//        }
//    }





}
