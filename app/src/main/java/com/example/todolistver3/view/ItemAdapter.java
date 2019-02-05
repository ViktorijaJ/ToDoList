package com.example.todolistver3.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.todolistver3.MainActivity;
import com.example.todolistver3.R;
import com.example.todolistver3.model.ItemVO;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<ItemVO> {


    public ItemAdapter(Context context, List<ItemVO> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemVO item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }


        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        Button deleteButton = (Button) convertView.findViewById(R.id.deleteBtn);

        MainActivity activity = (MainActivity) getContext();

        checkBox.setOnClickListener(activity);
        deleteButton.setOnClickListener(activity);

        checkBox.setText(item.title);
        checkBox.setChecked(item.isdone);

        return convertView;
    }
}
