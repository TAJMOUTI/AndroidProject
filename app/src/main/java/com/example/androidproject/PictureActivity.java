package com.example.androidproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;


public class PictureActivity extends AppCompatActivity {

    private String api_key = "f50c24a977bb49318a2f3ad3b2440b22";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "PictureActivity";
    private FirebaseUser user;
    private String USER_ID;
    private String photoPath = null;
    private Bitmap bitmap;
    private ImageView photoPlate;
    private ListView listIngredients;
    private ProgressBar progressBar;
    private ArrayList<String> ingredients;
    private Button saveButton;
    private FirebaseFirestore db;

    /**
     * We need a class that extends AsyncTask to run the API
     * call : new ClarifaiTask().execute(); to analyse the photo stored in variable : bitmap
     * .execute(); on a ClarifaiTask object will call the function : doInBackground()
     */
    private class ClarifaiTask extends AsyncTask {

        @Override
        protected ArrayList<String> doInBackground(Object[] objects) {
            if (bitmap != null) {
                // the list where we store the ingredients
                ingredients = new ArrayList<String>();

                // found the ingredients with the API
                final ClarifaiClient client = new ClarifaiBuilder(api_key).buildSync();
                Model<Concept> generalModel = client.getDefaultModels().foodModel();
                PredictRequest<Concept> request = generalModel.predict().withInputs(
                        ClarifaiInput.forImage(bitmapToBytes(bitmap))
                );
                List<ClarifaiOutput<Concept>> result = request.executeSync().get();

                // check the result
                if (!result.isEmpty()) {
                    // store the data in our list
                    for(int i = 0; i < result.size(); i++){
                        for(int j = 0; j < result.get(i).data().size(); j++){
                            String ingredient = result.get(i).data().get(j).name();
                            ingredients.add(ingredient);
                        }
                    }

                    // return the ingredients
                    return ingredients;
                } else {
                    Toast.makeText(getApplicationContext(), "nothing to see...", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("ANALYSE_PHOTO", "variable bitmap is null");
            }
            return null;
        }

        /**
         * this functioin is executed after doInBackground()
         * @param o : it contain all the ingredients names
         */
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // we display the ingredients on the screen
            progressBar.setVisibility(View.INVISIBLE);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PictureActivity.this,android.R.layout.simple_list_item_1, (ArrayList<String>) o);
            listIngredients.setAdapter(arrayAdapter);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // set variables
        user = FirebaseAuth.getInstance().getCurrentUser();
        USER_ID = user.getUid();
        listIngredients = findViewById(R.id.ingredients);
        photoPlate = findViewById(R.id.photoPlate);
        saveButton = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();

        // onclick listeners
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Map<String, Object> notebook = new HashMap<>();
            Log.d(TAG, "onClick: " + ingredients.toString());
            notebook.put("list", ingredients);
            notebook.put("date", new Date());

            db.collection("users").document(USER_ID).collection("notebook").add(notebook)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(PictureActivity.this, "Saved !", Toast.LENGTH_SHORT).show();
                    finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PictureActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
            }
        });

        openCamera();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            // start the camera activity
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            // when the photo will be taken, the program will go in : onActivityResult()
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // get extra
            Bundle extras = data.getExtras();
            // save photo
            bitmap = (Bitmap) extras.get("data");
            // display photo
            photoPlate.setImageBitmap(bitmap);
            // analyse the photo
            new ClarifaiTask().execute();
        } else {
            Intent intent = new Intent(getApplicationContext(), Main_Page.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    /**
     * convert a Bitmap to a Byte array
     * @param myPhoto : the bitmap picture we want to convert
     * @return : a byte array of the bitmap picture we sent
     */
    public byte[] bitmapToBytes(Bitmap myPhoto) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        myPhoto.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] myBytes = bos.toByteArray();
        return myBytes;
    }
}