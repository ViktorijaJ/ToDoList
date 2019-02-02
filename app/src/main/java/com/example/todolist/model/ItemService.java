package com.example.todolist.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemService extends SQLiteOpenHelper {

    public static final String DB_NAME = "lt.demo.todo.db";
    public static final int DB_VERSION = 1;

    ICallBackInterface objectWithOnSuccess;

    private class TasksTable implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_DONE = "done";
        public static final String COL_TASK_TITLE = "title";
    }

    public ItemService(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TasksTable.TABLE + " ( " +
                TasksTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TasksTable.COL_TASK_DONE + " INTEGER DEFAULT 0," +
                TasksTable.COL_TASK_TITLE + " TEXT NOT NULL" +
                ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TasksTable.TABLE);
        onCreate(db);
    }


    public void get() {
        Call<List<ItemVO>> call = itemsRepository.getAll();
        call.enqueue(this);
    }

    public void post(String task) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(TasksTable.COL_TASK_TITLE, task);
//        db.insertWithOnConflict(TasksTable.TABLE,
//                null,
//                values,
//                SQLiteDatabase.CONFLICT_REPLACE);
//        db.close();

        Call<ItemVO> call = itemsRepository.post(itemVO);
        call.enqueue(new Callback<ItemVO>() {
            @Override
            public void onResponse(Call<ItemVO> call, Response<ItemVO> response) {
                get();
            }

            @Override
            public void onFailure(Call<ItemVO> call, Throwable t) {
            }
        });
    }

    public void put(ItemVO itemVO) {
        Call<ItemVO> call = itemsRepository.put(itemVO);
        call.enqueue(new Callback<ItemVO>() {
            @Override
            public void onResponse(Call<ItemVO> call, Response<ItemVO> response) {
                get();
            }

            @Override
            public void onFailure(Call<ItemVO> call, Throwable t) {
            }
        });
    }

    public void delete(ItemVO itemVO) {
        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                get();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                get();
            }
        };

        Call<ResponseBody> call = itemsRepository.delete(itemVO.id);
        call.enqueue(callback);
    }


    @Override
    public void onResponse(Call<List<ItemVO>> call, Response<List<ItemVO>> response) {
        this.objectWithOnSuccess.onSuccess(response.body());
    }

    @Override
    public void onFailure(Call<List<ItemVO>> call, Throwable t) {

    }


}
