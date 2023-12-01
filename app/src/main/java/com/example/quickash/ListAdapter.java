package com.example.quickash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Transaction> {

    public ListAdapter(Context context, ArrayList<Transaction> userArrayList){

        super(context,R.layout.list_item,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Transaction user = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        }

        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView trans = convertView.findViewById(R.id.transac);
        TextView datetime = convertView.findViewById(R.id.lastMessage);
        TextView val = convertView.findViewById(R.id.msgtime);

        imageView.setImageResource(user.imageId);
        trans.setText(user.name1);
        datetime.setText(user.lastMessage);
        val.setText(user.lastMsgTime);


        return convertView;
    }
}
