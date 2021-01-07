package com.example.androidproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;


public class PictureActivity extends AppCompatActivity {

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
                ArrayList<String> labels = new ArrayList<String>();

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
                            String labelResult = result.get(i).data().get(j).name();
                            labels.add(labelResult);
                        }
                    }

                    // return the labels
                    return labels;
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
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PictureActivity.this,android.R.layout.simple_list_item_1, (ArrayList<String>) o);
            listIngredients.setAdapter(arrayAdapter);
        }
    }


    private String api_key = "f50c24a977bb49318a2f3ad3b2440b22";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String photoPath = null;
    private Bitmap bitmap;
    private ImageView photoPlate;
    private ListView listIngredients;
    private ArrayList<String> ingredients;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // set variables
        listIngredients = findViewById(R.id.ingredients);
        photoPlate = findViewById(R.id.photoPlate);
        nextButton = findViewById(R.id.nextButton);

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