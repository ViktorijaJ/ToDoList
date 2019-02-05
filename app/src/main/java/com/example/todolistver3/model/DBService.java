package com.example.todolistver3.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;


public class DBService extends SQLiteOpenHelper implements DataCrud {

    public static final String DB_NAME = "lt.todo2.db";
    public static final int DB_VERSION = 1;
    private com.example.todolistver3.model.ICallBackInterface callback;

    public DBService(Context context, com.example.todolistver3.model.ICallBackInterface callback) {
        super(context, DB_NAME, null, DB_VERSION);
        this.callback = callback;
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
        ArrayList<ItemVO> taskList = new ArrayList<ItemVO>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TasksTable.TABLE,
                new String[]{
                        TasksTable._ID,
                        TasksTable.COL_TASK_DONE,
                        TasksTable.COL_TASK_TITLE
                },
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(TasksTable._ID);
            int doneIndex = cursor.getColumnIndex(TasksTable.COL_TASK_DONE);
            int titleIndex = cursor.getColumnIndex(TasksTable.COL_TASK_TITLE);

            ItemVO itemVO = new ItemVO();
            itemVO.setId(cursor.getLong(idIndex));
            itemVO.setIsdone(cursor.getInt(doneIndex) != 0);
            itemVO.setTitle(cursor.getString(titleIndex));

            taskList.add(itemVO);
        }
        cursor.close();
        db.close();
        callback.onSuccess(taskList);
    }


    public void post(ItemVO task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksTable.COL_TASK_TITLE, task.title);
        db.insertWithOnConflict(TasksTable.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        get();
    }

    public void delete(ItemVO itemVO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TasksTable.TABLE,
                TasksTable._ID + " = ?",
                new String[]{Long.toString(itemVO.getId())});
        db.close();
        get();
    }

    public void put(ItemVO itemVO) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksTable.COL_TASK_DONE, itemVO.isdone);
        SQLiteDatabase db = this.getWritableDatabase();
        db.updateWithOnConflict(TasksTable.TABLE,
                contentValues,
                TasksTable._ID + " = ?",
                new String[]{Long.toString(itemVO.getId())},
                SQLiteDatabase.CONFLICT_REPLACE
        );
        db.close();
        get();
    }


    private class TasksTable implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_DONE = "done";
        public static final String COL_TASK_TITLE = "title";
    }

}