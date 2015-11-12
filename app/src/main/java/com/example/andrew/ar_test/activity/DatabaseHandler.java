package com.example.andrew.ar_test.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 11/10/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "placesManager";

    // Contacts table name
    private static final String TABLE_PLACES = "places";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAT = "latitutde";
    private static final String KEY_LNG = "longitude";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_PLACES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_LAT + " DOUBLE" + KEY_LNG + " DOUBLE" +")";
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        // Create tables again
        onCreate(db);
    }

    public void addLocation(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, place.getName()); //Location Name
        values.put(KEY_LAT, place.getlat()); // Get lat
        values.put(KEY_LNG, place.getlng()); // Get lng

        // Inserting Row
        db.insert(TABLE_PLACES, null, values);
        db.close(); // Closing database connection
    }

    // Adding new contact
    public void addPlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, place.getName()); // Place Name
        values.put(KEY_LAT, place.getlat()); // lat
        values.put(KEY_LNG, place.getlng());//long

        // Inserting Row
        db.insert(TABLE_PLACES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Place getPlace(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLACES, new String[]
                        { KEY_ID,
                                KEY_NAME, KEY_LAT, KEY_LNG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Place place = new Place(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), Double.parseDouble(cursor.getString(3)));

        // return place
        return place;
    }

    // Getting All Places
    public List<Place> getAllPlaces() {
        List<Place> placeList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setID(Integer.parseInt(cursor.getString(0)));
                place.setName(cursor.getString(1));
                place.selat(Double.parseDouble(cursor.getString(2)));
                place.setlng(Double.parseDouble(cursor.getString(3)));
                // Adding places to list
                placeList.add(place);
            } while (cursor.moveToNext());
        }

        // return place list
        return placeList;
    }

    // Getting contacts Count
    public int getPlacesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PLACES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating single contact
    public int updatePlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, place.getName());
        values.put(KEY_LAT,place.getlat());
        values.put(KEY_LNG,place.getlng());

        // updating row
        return db.update(TABLE_PLACES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(place.getID()) });
    }

    // Deleting single contact
    public void deletePlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, KEY_ID + " = ?",
                new String[]{String.valueOf(place.getID())});
        db.close();
    }
}