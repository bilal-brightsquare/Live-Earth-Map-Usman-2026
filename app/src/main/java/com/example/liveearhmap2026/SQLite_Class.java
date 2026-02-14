package com.example.liveearhmap2026;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class SQLite_Class {
    public static List<String> list_title = new ArrayList<>();
    public static List<String> list_detail = new ArrayList<>();
    public  static List<String> list_latitude = new ArrayList<>();
    public static List<String> list_longitude = new ArrayList<>();
    public static List<String> list_ids = new ArrayList<>();
      public static SQLiteDatabase mydatabase;

    public static void create_db(Activity activity) {
        try {
            mydatabase = activity.openOrCreateDatabase("SAVED_ADDRESSES_DB", Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void create_table(Activity activity) {
        try {
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS SAVED_ADDRESSES_TABLE(label VARCHAR," + "address VARCHAR," + "latitude VARCHAR," + "longitude VARCHAR," + "id VARCHAR);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getall_addresses(Activity activity) {
        try {
            list_title.clear();
            list_detail.clear();
            list_latitude.clear();
            list_longitude.clear();
            list_ids.clear();
            String selectQuery = "SELECT  * FROM " + "SAVED_ADDRESSES_TABLE";
            @SuppressLint("Recycle")
            Cursor cursor = mydatabase.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    list_title.add(cursor.getString(0));
                    list_detail.add(cursor.getString(1));
                    list_latitude.add(cursor.getString(2));
                    list_longitude.add(cursor.getString(3));
                    list_ids.add(cursor.getString(4));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Toast.makeText(activity, "Record not found", Toast.LENGTH_SHORT).show();;
        }
    }

    public static void delete_item(Activity activity, String text) {
        try {
            mydatabase.delete("SAVED_ADDRESSES_TABLE", "id=" + "'" + text + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insert_data(String label, String address,String lat, String longi, String id){

        try {
            String insert_query = "INSERT INTO SAVED_ADDRESSES_TABLE(label,address,latitude,longitude,id)" +
                    "VALUES('" + label + "'," +
                    "'" + address + "'," +
                    "'" + lat + "'," +
                    "'" + longi + "'," +
                    "'" + id + "')";
            mydatabase.execSQL(insert_query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
