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

public class ZitenAdapter extends ArrayAdapter<Ziten> {

    private LayoutInflater layoutinflater;
//    public CheckBox checkBox;
    public int checkbox;


    ZitenAdapter(Context context, int textViewResourceId, List<Ziten> objects,int checkbox_status) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkbox       = checkbox_status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Ziten ziten = getItem(position);

        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.home_item, null);
        }

        TextView titleText   = (TextView) convertView.findViewById(R.id.titleText);
        TextView contentText = (TextView) convertView.findViewById(R.id.contentText);
        CheckBox checkBox    = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        if (checkbox == 0) {
            checkBox.setVisibility(View.INVISIBLE);
        }
        else if(checkbox == 1){
            checkBox.setVisibility(View.VISIBLE);
        }
//        if(position % 2 == 0) {
//            checkBox.setVisibility(View.GONE);
//        }else{
//            checkBox.setVisibility(View.VISIBLE);
//        }

//        titleText.setText(ziten.title);
        //注意
//        titleText.setText(String.valueOf(ziten.groupId));
        contentText.setText(ziten.content);

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
