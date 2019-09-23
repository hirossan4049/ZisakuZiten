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
        final Ziten ziten1 = getItem(position);

        TextView titleText = (TextView) convertView.findViewById(R.id.titleText);
        TextView contentText = (TextView) convertView.findViewById(R.id.contentText);
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.weatherstatus_imageView);
//        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
//        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);


        titleText.setText(ziten.title);
        contentText.setText(ziten.content);

        return convertView;

    }
}
