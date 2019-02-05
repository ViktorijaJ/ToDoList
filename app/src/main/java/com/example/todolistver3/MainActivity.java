package com.example.todolistver3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.todolistver3.model.DBService;
import com.example.todolistver3.model.DataCrud;
import com.example.todolistver3.model.FileService;
import com.example.todolistver3.model.ItemVO;
import com.example.todolistver3.view.ItemAdapter;

import java.util.List;

import static com.example.todolistver3.model.FileService.FILE_NAME;

public class MainActivity extends AppCompatActivity implements com.example.todolistver3.model.ICallBackInterface, View.OnClickListener {

    private DataCrud dataCrud;
    private ItemAdapter itemAdapter;
    private String sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Using phone storage to save data");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        //by default app uses phone's storage to save data
        dataCrud = new DBService(this, this);
        dataCrud.get();
    }


    public void showDialog() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add something new")
                .setMessage("What do you want to do??")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        ItemVO itemVO = new ItemVO();
                        itemVO.title = task;
                        itemVO.setIsdone(false);
                        dataCrud.post(itemVO);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Get menu inflater.
        MenuInflater menuInflater = getMenuInflater();
        // Inflate the menu with menu items.
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Triggered when use click menu item. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        // Get menu item id.
        int itemId = item.getItemId();

        if (itemId == R.id.db) {
            dataCrud = new DBService(this, this);
            sub = "Using phone storage to save data";
            Toast.makeText(this, "Phone memory is set", Toast.LENGTH_LONG).show();
        } else if (itemId == R.id.file) {
            dataCrud = new FileService(this, this);
            sub = "Using file " + FILE_NAME + " to save data";
            Toast.makeText(this, "File memory is set", Toast.LENGTH_LONG).show();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(sub);
        dataCrud.get();
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
