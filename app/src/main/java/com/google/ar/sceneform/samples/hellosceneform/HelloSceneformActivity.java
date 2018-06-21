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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Plane.Type;
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

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ViewRenderable panelRenderable;
    private ModelRenderable placeholderMat;

    //Button and textviews to display item information
    private EditText itemName;
    private EditText itemLocation;
    private EditText itemLabel;
    private Switch switch1;

    int buttonCount = 1;

    MyDBHandler dbHandler;

    //Testing
    TextView myTV;


    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        //Set the views of the EditTexts
        itemName = findViewById(R.id.etName);
        itemLocation = findViewById(R.id.etLocation);
        itemLabel = findViewById(R.id.etLabel);

        //Testing
        myTV = findViewById(R.id.tryTV);


        //Database
        dbHandler = new MyDBHandler(this, null, null, 1);


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                    if (plane.getType() != Type.HORIZONTAL_UPWARD_FACING) {
                        return;
                    }


                    createIndividualRenderable();


                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());


                    // Create the transformable panel? and add it to the anchor.
                    // This panel should be used to display the information about the item after a tap on the existing object
                    TransformableNode panel = new TransformableNode(arFragment.getTransformationSystem());
                    panel.setParent(anchorNode);
                    panel.setRenderable(panelRenderable);
                    panel.select();


                });


    }


    //Create the renderable(button) in a new layout
    public void createIndividualRenderable() {

        LinearLayout newLinearLayout = new LinearLayout(this);
        Button newButton = new Button(this);
        newButton.setText(String.valueOf(buttonCount));

        newLinearLayout.addView(newButton);

        ViewRenderable.builder()
                .setView(this, newLinearLayout)
                .build()
                .thenAccept(renderable -> panelRenderable = renderable);

        buttonCount = buttonCount + 1;
    }


    //Displays the information of the current node
    public void toastOfItemInfo() {
        String dbString = dbHandler.databaseToString();
        Toast.makeText(this, dbString, Toast.LENGTH_LONG).show();
    }


    //Add information to the database
    public void addTheItem(String name, String location, String label){
        itemInformation theItemInfo = new itemInformation(name, location, label);
        dbHandler.addItem(theItemInfo);
        Toast.makeText(this, "Item successfully added!", Toast.LENGTH_SHORT).show();
    }

    //Delete item from database
    public void deleteItemFromDB(String name){
        dbHandler.deleteItem(name);
        Toast.makeText(this, "Item successfully deleted!", Toast.LENGTH_SHORT).show();
    }

    //Testing
    public void addOnClick(View view) {
        addTheItem(itemName.getText().toString(), itemLocation.getText().toString(), itemLabel.getText().toString());

        String dbString = dbHandler.databaseToString();
        //myTV.setText(dbString);
    }
}
