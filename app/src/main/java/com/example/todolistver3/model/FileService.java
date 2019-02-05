package com.example.todolistver3.model;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FileService implements DataCrud {


    public static final String FILE_NAME = "myToDo.txt";
    private com.example.todolistver3.model.ICallBackInterface callback;
    private List<ItemVO> list;
    private Context context;

    public FileService(ICallBackInterface callback, Context context) {
        super();
        this.callback = callback;
        this.context = context;
    }

    private void openFile() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);

            FileWriter writer = new FileWriter(file, true);
            // writer.append(sBody + "\n\n");
            writer.flush();
            writer.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void get() {
        list = new ArrayList<>();
        File file = new File(context.getFilesDir(), FILE_NAME);
        FileReader reader;
        try {
            reader = new FileReader(file);
            Scanner sc = new Scanner(reader);

            while (sc.hasNext()) {             //nuskaitys kiekviena eilute
                String line = sc.nextLine(); // id|isDone|title\n

                String[] values = line.split("\\|");
                ItemVO item = new ItemVO();
                item.setTitle(values[2]);
                item.setId(Long.parseLong(values[0]));
                item.setIsdone(Boolean.parseBoolean(values[1]));
                list.add(item);                //prideda i list visa nauja item
            }
            sc.close();

            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        callback.onSuccess(list);
    }

    private void saveList() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        FileWriter writer;
        try {
            writer = new FileWriter(file, false);

            String prefix = ""; // eilutes pradzia

            for (ItemVO item : list) {
                try {
                    writer.append(prefix + item.getId().toString() + "|" + item.isIsdone() + "|" + item.getTitle());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                prefix = "\n";
            }

            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void post(ItemVO task) {
        task.setId(new Date().getTime());
        list.add(task);

        saveList();
        get();
    }

    @Override
    public void put(ItemVO item) {
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            ItemVO temp = list.get(i);
            if (temp.getId().equals(item.getId())) {
                index = i;
                break;
            }
        }
        list.set(index, item);

        saveList();
        get();
    }

    @Override
    public void delete(ItemVO item) {
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            ItemVO temp = list.get(i);
            if (temp.getId().equals(item.getId())) {
                index = i;
                break;
            }
        }

        list.remove(index);


        saveList();
        get();
    }


}