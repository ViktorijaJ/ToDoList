package com.example.todolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.todolist.model.DBService;
import com.example.todolist.model.DataCrud;
import com.example.todolist.model.ItemVO;
import com.example.todolist.view.ItemAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements com.example.todolist.model.ICallBackInterface, View.OnClickListener {

    private DataCrud dataCrud;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        dataCrud = new DBService(this, this);
        dataCrud.get();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void showDialog() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Pridėti naują įrašą")
                .setMessage("Ką norite dar nuveikti?")
                .setView(taskEditText)
                .setPositiveButton("Pridėti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        ItemVO itemVO = new ItemVO();
                        itemVO.title = task;
                        itemVO.setIsdone(false);
                        dataCrud.post(itemVO);
                    }
                })
                .setNegativeButton("Atšaukti", null)
                .create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(List<ItemVO> list) {
        itemAdapter = new ItemAdapter(this, list);
        ListView listView = (ListView) this.findViewById(R.id.listView);
        listView.setAdapter(itemAdapter);
    }

    @Override
    public void onClick(View v) {
        ListView list = (ListView) v.getParent().getParent();

        int position = list.getPositionForView(v);

        ItemVO itemVO = itemAdapter.getItem(position);

        switch (v.getId()) {
            case R.id.deleteBtn:
                dataCrud.delete(itemVO);
                break;
            case R.id.checkbox:
                itemVO.setIsdone(!itemVO.isIsdone());
                dataCrud.put(itemVO);
                break;
        }
    }
}
