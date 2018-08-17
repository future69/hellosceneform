/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
//irene
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Plane.Type;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();

    private ArFragment arFragment;
    private ViewRenderable panelRenderable3;
    private ViewRenderable panelRenderable2;
    private ViewRenderable panelRenderable;
    private EditText itemName;
    private EditText itemLocation;
    private EditText itemLabel;
    private EditText itemSearch;

    private Button databaseBT;
    private Button restartBT;
    private Button loadPointsBT;

    int buttonCount = 1;

    MyDBHandler dbHandler;

    TextView myTV;
    AnchorNode anchorNode;
    Vector3 vec3AnchorPos;
    Vector3 vec3AnchorLocalPos;

    Vector3 reloadedVector3;

    AnchorNode testAN;

    float coordinatesX;
    float coordinatesY;
    float coordinatesZ;

    Spinner spinner;

    ArrayAdapter<CharSequence> adapter;


    //Testing
    int COUNT = 3;
    Switch idSwitch;

    int resultInt = 0;
    int realResult = 0;
    int aaaCOUNT = 0;


    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        //Testing
        myTV = findViewById(R.id.showWhichPointTV);

        //Set views of buttons
        databaseBT = findViewById(R.id.dbButton);
        restartBT = findViewById(R.id.restartButton);
        loadPointsBT = findViewById(R.id.loadNode);

        //Set the views of the EditTexts
        itemName = findViewById(R.id.etName);
        itemLocation = findViewById(R.id.etLocation);
        itemLabel = findViewById(R.id.etLabel);
        itemSearch = findViewById(R.id.etSearch);

        //Set view of spinner and array adapter
        spinner = (Spinner)findViewById(R.id.spinner1);
        adapter = ArrayAdapter.createFromResource(this, R.array.areaLocations, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Set switch
        idSwitch = (Switch)findViewById(R.id.idSwitch);

        //Database
        dbHandler = new MyDBHandler(this, null, null, 1);

        //Check for existing button count
        //checkForExistingButtonCount();

        //Necessary preloads
        createIndividualRenderable();
        loadIndividualNodeRenderable(COUNT);
        //loadNodeForSearchBar(1);
        //loadNodeForSearchBar(3);

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                    if (plane.getType() != Type.HORIZONTAL_UPWARD_FACING) {
                        return;
                    }

                    else if(TextUtils.isEmpty(itemName.getText().toString())) {
                        itemName.setError("Please input a item name");
                        return;
                    }

                    else if(TextUtils.isEmpty(itemLocation.getText().toString())) {
                        itemLocation.setError("Please input a item location");
                        return;
                    }

                    else if(TextUtils.isEmpty(itemLabel.getText().toString())) {
                        itemLabel.setError("Please input a item label");
                        return;
                    }

                    //To create the renderable in the AR plane
                    createIndividualRenderable();

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    //Vector 3  world position
                    vec3AnchorPos = anchorNode.getWorldPosition();
                    vec3AnchorLocalPos = anchorNode.getLocalPosition();

                    coordinatesX = vec3AnchorPos.x;
                    coordinatesY = vec3AnchorPos.y;
                    coordinatesZ = vec3AnchorPos.z;


                    //To add the data to database on tap on a valid surface
                    tapAndAdd();

                    // Create the transformable panel and add it to the anchor.
                    // This panel should be used to display the information about the item after a tap on the existing object
                    TransformableNode panel = new TransformableNode(arFragment.getTransformationSystem());
                    panel.setParent(anchorNode);
                    panel.setRenderable(panelRenderable);
                    panelRenderable.setPixelsToMetersRatio(2000);
                    panel.select();


                    itemName.setText(null);
                    itemLocation.setText(null);
                    itemLabel.setText(null);

                });



        //On text change for search ET
        itemSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String itemSearchText = itemSearch.getText().toString();
                String result = String.valueOf(dbHandler.searchName(itemSearchText, spinner.getSelectedItem().toString()));
                resultInt = dbHandler.searchName(itemSearchText, spinner.getSelectedItem().toString());

                if(result != null && resultInt != 0){

                    myTV.setText("");
                    myTV.setText("Point " + result);
                    //realResult = Integer.parseInt(result);
                    //loadNodeForSearchBar(realResult);

                } else{
                    myTV.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Spinner on item selected listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (aaaCOUNT != 0) {
                    //checkForExistingButtonCount();
                }
                aaaCOUNT++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Create the renderable(button) in a new layout
    public void createIndividualRenderable() {

        LinearLayout newLinearLayout = new LinearLayout(this);
        Button newButton = new Button(this);
        newButton.setText(String.valueOf(buttonCount));

        newButton.setId(buttonCount);
        newLinearLayout.addView(newButton);
        newButton.setBackground(getResources().getDrawable(R.drawable.roundbutton));
        newButton.setTextSize(30);

        newButton.setOnClickListener(view -> {

            int theCount = newButton.getId();
            showData2(theCount + 2);
        });

        ViewRenderable.builder()
                .setView(this, newLinearLayout)
                .build()
                .thenAccept(renderable -> panelRenderable = renderable);


        //Keeps track
        buttonCount = buttonCount + 1;
    }


    //Delete irene from database
    public void deleteItemFromDB(String name){
        dbHandler.deleteItem(name);
        Toast.makeText(this, "Item successfully deleted!", Toast.LENGTH_SHORT).show();
    }

    //Testing (Add on click is only for clear button)
    public void viewDB(View view) {
        //clearAllData();
        startActivity(new Intent(this, dbInfo.class));
    }

    //Runs the background thread which is basically adding the data to the database
    public void tapAndAdd(){
        new doInBackground().execute();

    }

    //Display data from database to UI
    public void showData(){
        String dbString = dbHandler.databaseToString();
        myTV.setText(dbString);
    }

    public void showData2(int theCount){

        String dbString = dbHandler.databaseToString2(theCount, spinner.getSelectedItem().toString());
        //myTV.setText(dbString);
        Toast.makeText(this, dbString, Toast.LENGTH_LONG).show();
    }


    //Testing purposes to clear the data
    public void clearAllData(){
        dbHandler.clearAll();
    }

    public void loadIndividualNodeRenderable(int lCount){
        LinearLayout newLinearLayout1 = new LinearLayout(this);
        Button newButton = new Button(this);
        newButton.setText(String.valueOf(lCount - 2));

        newButton.setId(lCount - 2);
        newButton.setBackground(getResources().getDrawable(R.drawable.roundbutton));
        newButton.setTextSize(30);
        newLinearLayout1.addView(newButton);

        newButton.setOnClickListener(view -> {

            int theCount = newButton.getId();
            showData2(theCount + 2);

        });

        ViewRenderable.builder()
                .setView(this, newLinearLayout1)
                .build()
                .thenAccept(renderable -> panelRenderable2 = renderable);


    }

    public void loadNode(View view) {

        int x = COUNT;

        String pointX = dbHandler.retrieveVector3DataX(x, spinner.getSelectedItem().toString());
        String pointY = dbHandler.retrieveVector3DataY(x, spinner.getSelectedItem().toString());
        String pointZ = dbHandler.retrieveVector3DataZ(x, spinner.getSelectedItem().toString());

        if(pointX == "") {
            return;
        }

        COUNT++;

        Float pointXFloat = Float.valueOf(pointX);
        Float pointYFloat = Float.valueOf(pointY);
        Float pointZFloat = Float.valueOf(pointZ);

        reloadedVector3 = new Vector3(pointXFloat, pointYFloat, pointZFloat);

        loadIndividualNodeRenderable(COUNT);

        testAN = new AnchorNode();
        testAN.setParent(arFragment.getArSceneView().getScene());
        testAN.setWorldPosition(reloadedVector3);
        panelRenderable2.setPixelsToMetersRatio(2000);
        testAN.setRenderable(panelRenderable2);


        pointXFloat = null;
        pointYFloat = null;
        pointZFloat = null;

        buttonCount = COUNT - 2;

        //Makes it so that if you press the load node buttons,
        //it overrides the previous createIndividualRenederable
        //in the OnCreate
        createIndividualRenderable();

    }

//    public void loadNodeForSearchBar(int y) {
//
//        y = y + 2;
//
//        String pointX = dbHandler.retrieveVector3DataX(y, spinner.getSelectedItem().toString());
//        String pointY = dbHandler.retrieveVector3DataY(y, spinner.getSelectedItem().toString());
//        String pointZ = dbHandler.retrieveVector3DataZ(y, spinner.getSelectedItem().toString());
//
//        if(pointX == "") {
//            return;
//        }
//
//        Float pointXFloat = Float.valueOf(pointX);
//        Float pointYFloat = Float.valueOf(pointY);
//        Float pointZFloat = Float.valueOf(pointZ);
//
//        reloadedVector3 = new Vector3(pointXFloat, pointYFloat, pointZFloat);
//
//        loadIndividualNodeRenderableForSearchBar(y);
//
//
//        testAN = new AnchorNode();
//        testAN.setParent(arFragment.getArSceneView().getScene());
//        testAN.setWorldPosition(reloadedVector3);
//        testAN.setRenderable(panelRenderable3);
//
//        pointXFloat = null;
//        pointYFloat = null;
//        pointZFloat = null;
//
//
//    }
//
//    public void loadIndividualNodeRenderableForSearchBar(int lCount) {
//
//
//        LinearLayout newLinearLayout1 = new LinearLayout(this);
//        Button newButton = new Button(this);
//        newButton.setText(String.valueOf(lCount - 2));
//
//        newButton.setId(lCount - 2);
//        newButton.setBackground(getResources().getDrawable(R.drawable.roundbutton));
//        newButton.setTextSize(30);
//        newLinearLayout1.addView(newButton);
//
//        newButton.setOnClickListener(view -> {
//
//            int theCount = newButton.getId();
//            showData2(theCount + 2);
//
//        });
//
//        ViewRenderable.builder()
//                .setView(this, newLinearLayout1)
//                .build()
//                .thenAccept(renderable -> panelRenderable3 = renderable);
//
//    }



    public void restartActivity(View view) {

        recreate();

    }


    public void changeView(View view) {

        if(idSwitch.isChecked()){
            //When the switch is activated

            itemName.setVisibility(itemName.GONE);
            itemLabel.setVisibility(itemLabel.GONE);
            itemLocation.setVisibility(itemLocation.GONE);

            spinner.setVisibility(spinner.VISIBLE);
            databaseBT.setVisibility(databaseBT.GONE);
            restartBT.setVisibility(restartBT.GONE);
            loadPointsBT.setVisibility(loadPointsBT.GONE);

            itemSearch.setVisibility(itemSearch.VISIBLE);

            myTV.setText("");
            myTV.setVisibility(myTV.VISIBLE);

        }

        else if(!idSwitch.isChecked()){
            //When the switch is not activated

            itemName.setVisibility(itemName.VISIBLE);
            itemLabel.setVisibility(itemLabel.VISIBLE);
            itemLocation.setVisibility(itemLocation.VISIBLE);

            spinner.setVisibility(spinner.VISIBLE);
            databaseBT.setVisibility(databaseBT.VISIBLE);
            restartBT.setVisibility(restartBT.VISIBLE);
            loadPointsBT.setVisibility(loadPointsBT.VISIBLE);

            itemSearch.setVisibility(itemSearch.GONE);

            myTV.setVisibility(myTV.GONE);

        }

    }


    public void checkForExistingButtonCount(){
        int x = dbHandler.checkButtonCount(spinner.getSelectedItem().toString());

        if (x != 0){

            buttonCount = x - 1;

        }
        else if(x == 0){
            buttonCount = 1;
        }
    }






    //Run code in background (AsyncTask)
    private class doInBackground extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {


            //Add information to the database
            itemInformation theItemInfo = new itemInformation(itemName.getText().toString(), itemLocation.getText().toString(), itemLabel.getText().toString(), spinner.getSelectedItem().toString() , buttonCount, coordinatesX, coordinatesY, coordinatesZ);
            dbHandler.addItem(theItemInfo);

            return null;



        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //showData();
        }
    }

}













