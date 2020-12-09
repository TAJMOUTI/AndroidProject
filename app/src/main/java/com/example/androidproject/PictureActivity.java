package com.example.androidproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
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


    String api_key = "f50c24a977bb49318a2f3ad3b2440b22";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String photoPath = null;
    Bitmap bitmap;
    ImageView photoPlate;
    ListView listIngredients;
    ArrayList<String> ingredients;
    Button nextButton;


    // Ce qui va appeler l'API pour recuperer les ingredients et les placer dans une liste
    private class ClarifaiTask extends AsyncTask<File, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(File... images) {

            //In case we would like remove doubles :
            //HashSet<String> Labels = new HashSet<>();
            ArrayList<String> Labels = new ArrayList<String>();//Creation du tableau pour ranger les ingredients analysés par l'API

            ClarifaiClient client = new ClarifaiBuilder(api_key).buildSync();// Lancement de l'API
            List<ClarifaiOutput<Concept>> predictionResults;

            //Make prediction for each image in parameters
            for (File image : images) {
                bitmap = BitmapFactory.decodeFile(image.getPath()); //decode le chemin d'un fichier et cree un fichier bitmap
                Model<Concept> generalModel = client.getDefaultModels().foodModel(); //utilise le modèle foodModel de Clarifai
                PredictRequest<Concept> request = generalModel.predict().withInputs(
                        ClarifaiInput.forImage("https://samples.clarifai.com/food.jpg")//Lien vers une image comme exemple
                );
                List<ClarifaiOutput<Concept>> result = request.executeSync().get(); // Ajoute les résultats dans la liste result

                //On récupère les labels de chaque objet et on les place dans un tableau
                for(int i = 0; i < result.size(); i++){
                    for(int j = 0; j < result.get(i).data().size(); j++){
                        String labelResult = result.get(i).data().get(j).name();
                        Labels.add(labelResult);
                    }
                }
                //In case we would like to remove doubles :
                // ArrayLabels = new ArrayList<>(Labels);
            }
            System.out.println(Labels);
            return Labels;
        }

        protected void onPostExecute(final ArrayList<String> ObjectLabels) {
            System.out.println("on post execute");

            listIngredients = findViewById(R.id.ingredients);
            photoPlate = findViewById(R.id.photoPlate);
            nextButton = findViewById(R.id.nextButton);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PictureActivity.this,android.R.layout.simple_list_item_1, ObjectLabels);
            listIngredients.setAdapter(arrayAdapter);

            listIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO if item is already in the list
                    ingredients.add(ObjectLabels.get(position));
                    view.setBackgroundResource(R.color.myBlue);
                    //view.setBackgroundColor(0xB8B8D1); //TODO changer la methode de changement de couleur
                    view.setEnabled(false);

                    for(int i = 0; i < ingredients.size(); i++) {
                        System.out.println(ingredients.get(i));
                    }
                    if(ingredients.isEmpty()){
                        nextButton.setEnabled(false);
                        //nextButton.setBackgroundResource(R.color.myBlue); //TODO relier a la paged'ajout des quantité
                        //view.setBackgroundColor(0xB8B8D1); //TODO changer la methode de changement de couleur

                    }
                }
            });



            //Affichage de la photo crée
            photoPlate.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Quand la page se crée la fonction takePicture est directement crée
        takePicture();
        setContentView(R.layout.activity_picture);
        ingredients = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Si la photo a été prise on l'envoie à Clarifai pour vérifier
        if (photoPath != null) {
            System.out.println("photopath is okay");
            new ClarifaiTask().execute(new File(photoPath));
        }
    }
// La fonction pour prendre des photos
    public void takePicture() {
        //Ouvrir l'appareil photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //System.out.println(takePictureIntent.resolveActivity(getPackageManager()));
        //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
         File photoFile;
            try {
                //Essayer d'enregistrer la photo
                File storageDir = getFilesDir();//Recupère le dossier ou l'on veut enregistrer la photo
                photoFile = File.createTempFile(
                        "SNAPSHOT",
                        ".jpg",
                        storageDir
                );

                photoPath = photoFile.getAbsolutePath();//Recuperation de la route vers la photo prise
            } catch (IOException ex) {
                return;
            }

            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.androidproject.fileprovider",
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //}
    }

}