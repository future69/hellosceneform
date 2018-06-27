package com.google.ar.sceneform.samples.hellosceneform;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "itemInfo.db";
    public static final String TABLE_ITEMINFORMATION = "ItemInformation";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEMNAME = "itemName";
    public static final String COLUMN_ITEMLABEL = "itemLabel";
    public static final String COLUMN_ITEMLOCATION = "itemLocation";
    public static final String COLUMN_BUTTONCOUNT = "buttonCount";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_ITEMINFORMATION + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEMNAME + " TEXT, " +
                COLUMN_ITEMLABEL + " TEXT, " +
                COLUMN_ITEMLOCATION + " TEXT, " +
                COLUMN_BUTTONCOUNT + " INT " +
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMINFORMATION);
        onCreate(db);

    }

    public void clearAll (){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMINFORMATION);
        onCreate(db);
    }

    //Add a new row to the database
    public void addItem(itemInformation theItem){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEMNAME, theItem.get_itemName());
        values.put(COLUMN_ITEMLOCATION, theItem.get_itemLocation());
        values.put(COLUMN_ITEMLABEL, theItem.get_itemLabel());
        values.put(COLUMN_BUTTONCOUNT, theItem.get_buttonCount());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ITEMINFORMATION, null, values);
        db.close();
    }

    //Delete item from database by name
    public void deleteItem(String itemName){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ITEMINFORMATION + " WHERE " + COLUMN_ITEMNAME + "=\"" + itemName + "\";"  );

    }

    //Print out database as a String
    public String databaseToString(){

        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMINFORMATION + " WHERE 1";


        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);

        //Move to the first row in your results
        c.moveToNext();


        //Infinite while loop i think
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("itemName")) != null){
                dbString += c.getString(c.getColumnIndex("itemName"));
                //There may be some sort of continuous loop here
                dbString += "\n";
            }
            c.moveToNext();
        }

        db.close();
        return dbString;

    }


    //Print out database as a String //TESTING
    public String databaseToString2(int count){

        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMINFORMATION + " WHERE " + COLUMN_BUTTONCOUNT + " = " + count;


        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);

        //Move to the first row in your results
        c.moveToNext();


        //Infinite while loop i think
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("itemName")) != null){

                dbString += c.getString(c.getColumnIndex("itemName"));

                dbString += "\n";

                dbString += c.getString(c.getColumnIndex("itemLocation"));

                dbString += "\n";
            }
            c.moveToNext();
        }

        db.close();
        return dbString;

    }





}



















