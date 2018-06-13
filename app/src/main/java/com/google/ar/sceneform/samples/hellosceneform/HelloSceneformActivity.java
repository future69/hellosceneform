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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Plane.Type;
import com.google.ar.sceneform.AnchorNode;
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

  private TextView itemName;
  private TextView itemLocation;
  private TextView itemLabel;

  @Override
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  // CompletableFuture requires api level 24
  // FutureReturnValueIgnored is not valid
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ux);
    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);


    //Enables the retrieving of data from testpanel.xlm
      LayoutInflater inflater = getLayoutInflater();

      View vi = inflater.inflate(R.layout.testpanel, null); //testpanel.xml is your file.
      CheckBox cb1 = vi.findViewById(R.id.cbONE); //get a reference to the checkbox on the testpanel.xml file.

      //Builds the panel(Object), but it does not appear as you need to tap the screen for it to appear
      ViewRenderable.builder()
              .setView(this, R.layout.testpanel)
              .build()
              .thenAccept(renderable -> panelRenderable = renderable);

      //test for 3d model
      MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.YELLOW))
              .thenAccept(
                      material -> {
                          placeholderMat =
                                  ShapeFactory.makeSphere(0.1f, new Vector3(0.0f, 0.15f, 0.0f), material); });





    arFragment.setOnTapArPlaneListener(
        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
          if (panelRenderable == null) {
            return;
          }

          if (plane.getType() != Type.HORIZONTAL_UPWARD_FACING) {
            return;
          }

          // Create the Anchor.
          Anchor anchor = hitResult.createAnchor();
          AnchorNode anchorNode = new AnchorNode(anchor);
          anchorNode.setParent(arFragment.getArSceneView().getScene());

            //Original code
//          // Create the transformable andy and add it to the anchor.
//          TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
//          andy.setParent(anchorNode);
//          andy.setRenderable(andyRenderable);
//          andy.select();

            // Create the transformable panel? and add it to the anchor.
            //This panel should be used to display the infromation about the item after a tap on the existing object
            TransformableNode panel = new TransformableNode(arFragment.getTransformationSystem());
            panel.setParent(anchorNode);
            panel.setRenderable(panelRenderable);
            panel.select();


        });


  }

    //Check if the checkbox is ticked or not to display text
    public void cbOnClick(View view) {

        //Enables the retrieving of data from testpanel.xlm
        LayoutInflater inflater = getLayoutInflater();

        View vi = inflater.inflate(R.layout.testpanel, null); //testpanel.xml is your file.
        CheckBox cb1 = vi.findViewById(R.id.cbONE); //get a reference to the checkbox on the testpanel.xml file.

        itemName = vi.findViewById(R.id.itemName);
        itemLocation = vi.findViewById(R.id.itemLocation);
        itemLabel = vi.findViewById(R.id.itemLabel);

        if (cb1.isSelected()){

            itemName.setVisibility(View.VISIBLE);
            itemLocation.setVisibility(View.VISIBLE);
            itemLabel.setVisibility(View.VISIBLE);
        }

        else if (!cb1.isSelected()){

            itemName.setText(getString(R.string.ItemNameString2));
            itemLocation.setText(getString(R.string.ItemLocationString2));
            itemLabel.setText(getString(R.string.ItemLabelString2));

            itemName.setVisibility(View.VISIBLE);
            itemLocation.setVisibility(View.VISIBLE);
            itemLabel.setVisibility(View.VISIBLE);


        }

    }
}
