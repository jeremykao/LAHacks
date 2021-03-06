package com.lahacksrecipeapp.app;

/**
 * Created by jeremykao on 4/13/14.
 */

import android.content.ContentValues;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Pantry2Plate";

    // Contacts table name
    private static final String TABLE_NAME = "pantry";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_UNITS = "units";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT NOT NULL," + KEY_QUANTITY + " INTEGER,"
                + KEY_UNITS + " TEXT" + ")";
        db.execSQL(CREATE_PANTRY_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    void addToPantry(Item item ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> itemNames = this.getAllItemNames();
        if (!itemNames.contains(item.getName())){
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, item.getName()); // Name
            values.put(KEY_QUANTITY, item.getQuantity()); // Quantity
            values.put(KEY_UNITS, item.getUnits()); // Units

            // Inserting Row
            db.insert(TABLE_NAME, null, values);
        }
        db.close(); // Closing database connection
    }

    // Getting single item
    Item getItem (int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_NAME,
                KEY_QUANTITY, KEY_UNITS }, KEY_ID + "=?" + id, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item(cursor.getString(1),
                cursor.getInt(2), cursor.getString(3));

        // return item
        return item;
    }

    // Getting All Items
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(1),
                        cursor.getInt(2), cursor.getString(3));
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return contact list
        return itemList;
    }

    //get all item names
    public ArrayList<String> getAllItemNames() {
        ArrayList<String> itemList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                itemList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return contact list
        return itemList;
    }

    // Updating single item
    public void updateItem(String n, Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_NAME + " SET name = '"
                + item.getName() + "', quantity = " + item.getQuantity()
                + ", units = '" + item.getUnits()
                + "' WHERE name = '" + n + "';";
        Log.i("DBHandler update", updateQuery);
        Cursor cursor = db.rawQuery(updateQuery, null);
        Log.i("DBHandler update", "Cursor = " +cursor.getCount());
    }

    // Deleting single contact
    public void deleteItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_NAME + " = ?",
                new String[] { String.valueOf(name) });
        db.close();
    }


    // Getting contacts Count
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}
