package com.example.fpavot.projetandroid;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fpavot.projetandroid.R;
import com.example.fpavot.projetandroid.ResponseReceiver;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by fpavot on 30/03/2018.
 */

//service pour l'appel vers l'api
public class MonServiceLourd extends IntentService {

    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";


    public MonServiceLourd() {
        super("com.example.fpavot.projetandroid.MonServiceLourd");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Tâche à exectuer

        String s = intent.getStringExtra(PARAM_IN_MSG);

        //on essaye de se co à l'api FOOD2FORK (limite de 50 REQUETES PAR jour !)
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://food2fork.com/api/search?key=c7fed0f27db6d57141dcfa5013d965fa&q=" + s);
            connection = (HttpURLConnection) url.openConnection();

            //input stream pour recup le json
            InputStream inputStream = connection.getInputStream();
            BufferedReader bR = new BufferedReader(  new InputStreamReader(inputStream));
            String line = "";

            StringBuilder responseStrBuilder = new StringBuilder();
            while((line =  bR.readLine()) != null){

                responseStrBuilder.append(line);
            }
            inputStream.close();

            //on crée un objet json (library simpleJSON dans simple.)
            JSONObject jsonObject= new JSONObject(responseStrBuilder.toString());

            //on recup les recettes
            JSONObject j = new JSONObject(  jsonObject.optJSONArray("recipes").get(1).toString() );

            //on isole l'url de l'image et le titre de la recette
            String tText = j.get("title").toString();
            String UrlImg = j.get("image_url").toString();

            //on prépare la réponse
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(PARAM_OUT_MSG, tText);

            //on envoie le texte et l'image bitmap en appelant dlImage
            intent.putExtra("BitmapImage",dlImage(new URL(UrlImg)));
            sendBroadcast(broadcastIntent);

            //debug
            System.out.println( "OUUUUUUUUUUUUUUUUUTO");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //handle pour dl les images de l'api
    private Bitmap dlImage(URL url) {
        Bitmap bitmap = null;

//on se connecte à l'url de l'image
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            //on utilise un inputstream pour recup le bitmap, que l'on build dans un object
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);



        } catch (Exception e) {
            e.printStackTrace();
        }


        //et on retourne
        return bitmap;
    }
}
