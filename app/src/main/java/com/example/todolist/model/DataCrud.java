package com.example.todolist.model;

public interface DataCrud {
    void get();
    void post(String task);
    void put(ItemVO item);
    void delete(ItemVO item);
}