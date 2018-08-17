package com.google.ar.sceneform.samples.hellosceneform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class dbInfo extends AppCompatActivity {


    MyDBHandler dbHandler;

    TextView displayInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_info);

        //The textview for the database information
        displayInfo = findViewById(R.id.displayDbInfo);

        //Database
        dbHandler = new MyDBHandler(this, null, null, 1);

        showData();


    }


    //Display data from database to UI
    public void showData(){
        String dbString = dbHandler.databaseToString();
        displayInfo.setText(dbString);
    }


    public void clearOnClick(View view) {
        dbHandler.clearAll();

        String dbString = dbHandler.databaseToString();
        displayInfo.setText(dbString);
    }

    public void returnOnClick(View view) {

        startActivity(new Intent(this, HelloSceneformActivity.class));

    }

}
