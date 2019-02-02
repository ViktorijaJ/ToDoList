package com.example.todolist.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.demotodo.MainActivity;
import com.example.demotodo.R;
import com.example.demotodo.model.ItemVO;
import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.model.ItemService;
import com.example.todolist.model.ItemVO;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ItemVO> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    // Model
    private ItemService itemService;

    public ListAdapter(Context context, ArrayList<ItemVO> items, ItemService itemService) {
        super(context, 0, items);
        this.itemService = itemService;
    }

    @Override
    public void onClick(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        int position = listView.getPositionForView(parentRow);

        ItemVO itemVO = getItem(position);
        itemService.delete(itemVO);
        ((MainActivity) getContext()).updateView();
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();

        if (listView == null) {
            return;
        }

        int position = listView.getPositionForView(parentRow);

        ItemVO itemVO = getItem(position);

        if (isChecked == true) {
            itemVO.done = 1;
        } else {
            itemVO.done = 0;
        }
        itemService.put(itemVO);

        ((MainActivity) getContext()).updateView();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemVO item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }

        CheckBox itemCheckbox = (CheckBox) convertView.findViewById(R.id.checkbox);

        itemCheckbox.setText(item.title);
        itemCheckbox.setChecked(item.done == 1);

        itemCheckbox.setOnCheckedChangeListener(this);

        Button deleteButton = (Button) convertView.findViewById(R.id.deleteBtn);
        deleteButton.setOnClickListener(this);

        return convertView;
    }
}


}
