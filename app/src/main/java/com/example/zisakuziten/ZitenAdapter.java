package com.example.zisakuziten;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ZitenAdapter extends ArrayAdapter<Ziten> {

    private LayoutInflater layoutinflater;
    public CheckBox checkBox;

    ZitenAdapter(Context context, int textViewResourceId, List<Ziten> objects) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Ziten ziten = getItem(position);

        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.home_item, null);
        }

        TextView titleText = (TextView) convertView.findViewById(R.id.titleText);
        TextView contentText = (TextView) convertView.findViewById(R.id.contentText);
        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);
//        if(position % 2 == 0) {
//            checkBox.setVisibility(View.GONE);
//        }else{
//            checkBox.setVisibility(View.VISIBLE);
//        }

        titleText.setText(ziten.title);
        contentText.setText(ziten.content);

        return convertView;
    }

    public void checkbox_visible(){
        checkBox.setVisibility(View.VISIBLE);
    }





}
