package com.google.ar.sceneform.samples.hellosceneform;

//irene
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
    public static final String COLUMN_AREALOCATION = "areaLocation";
    public static final String COLUMN_BUTTONCOUNT = "buttonCount";
    public static final String COLUMN_POINTX = "pointX";
    public static final String COLUMN_POINTY = "pointY";
    public static final String COLUMN_POINTZ = "pointZ";


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
                COLUMN_AREALOCATION + " TEXT, " +
                COLUMN_BUTTONCOUNT + " INT, " +
                COLUMN_POINTX + " REAL, " +
                COLUMN_POINTY + " REAL, " +
                COLUMN_POINTZ + " REAL " +
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
        values.put(COLUMN_AREALOCATION, theItem.get_areaLocation());
        values.put(COLUMN_BUTTONCOUNT, theItem.get_buttonCount());
        values.put(COLUMN_POINTX, theItem.get_pointX());
        values.put(COLUMN_POINTY, theItem.get_pointY());
        values.put(COLUMN_POINTZ, theItem.get_pointZ());
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

        dbString += "Item information : ";



        //Infinite while loop i think
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("itemName")) != null){

                dbString += "\n";

                dbString += c.getString(c.getColumnIndex("itemName")) + ", ";

                dbString += c.getString(c.getColumnIndex("itemLocation")) + ", ";

                dbString += c.getString(c.getColumnIndex("itemLabel")) + ", ";

                dbString += c.getString(c.getColumnIndex("pointX")) + ", ";

                dbString += c.getString(c.getColumnIndex("pointY")) + ", ";

                dbString += c.getString(c.getColumnIndex("pointZ")) + ", ";

                dbString += c.getString(c.getColumnIndex("buttonCount")) + ", ";

                dbString += c.getString(c.getColumnIndex("areaLocation"));

                dbString += "\n";

            }
            c.moveToNext();
        }



        db.close();
        return dbString;

    }


    //Print out database as a String
    // Shows data on button press
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

                dbString += c.getString(c.getColumnIndex("itemLabel"));


            }
            c.moveToNext();
        }

        db.close();
        return dbString;

    }



    public String retrieveVector3DataX(int count, String areaLocation){
        String dbFloat = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMINFORMATION + " WHERE " + COLUMN_BUTTONCOUNT + " = " + count + " AND " + COLUMN_AREALOCATION + "='" + areaLocation + "'";

            //Cursor points to a location in your results
            Cursor c = db.rawQuery(query, null);
            //Move to the first row in your results
            c.moveToNext();
            //Infinite while loop i think
            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex("buttonCount")) != null) {

                    dbFloat += c.getString(c.getColumnIndex("pointX"));

                }
                //Dont change
                c.moveToNext();
            }
            db.close();
            return dbFloat;

    }


    public String retrieveVector3DataY(int count, String areaLocation){
        String dbFloat = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMINFORMATION + " WHERE " + COLUMN_BUTTONCOUNT + " = " + count + " AND " + COLUMN_AREALOCATION + "='" + areaLocation + "'";

            //Cursor points to a location in your results
            Cursor c = db.rawQuery(query, null);
            //Move to the first row in your results
            c.moveToNext();
            //Infinite while loop i think
            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex("buttonCount")) != null) {

                    dbFloat += c.getString(c.getColumnIndex("pointY"));

                }
                //Dont change
                c.moveToNext();
            }
            db.close();
        return dbFloat;
    }


    public String retrieveVector3DataZ(int count, String areaLocation){
        String dbFloat = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMINFORMATION + " WHERE " + COLUMN_BUTTONCOUNT + " = " + count + " AND " + COLUMN_AREALOCATION + "='" + areaLocation + "'";

            //Cursor points to a location in your results
            Cursor c = db.rawQuery(query, null);
            //Move to the first row in your results
            c.moveToNext();
            //Infinite while loop i think
            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex("buttonCount")) != null) {

                    dbFloat += c.getString(c.getColumnIndex("pointZ"));

                }
                //Dont change
                c.moveToNext();
            }
            db.close();
        return dbFloat;
    }



}



















