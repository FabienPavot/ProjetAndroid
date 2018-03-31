package com.example.fpavot.projetandroid;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//java pour l'activité principale
public class mainSearch extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    //dèrnières valeurs pour l'accéléromètre
    private long lastTime = 0;
    private float lastX, lastY, lastZ;

    private static final int LimiteSpd = 600; //détection accélération si dépasse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        //déclarations pour gérer l'accélérométeur
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    //gestion du bouton pour la recherche
    Button yourButton = (Button) findViewById(R.id.button2);

        yourButton.setOnClickListener(new View.OnClickListener(){ //listener pour le bouton
        public void onClick(View v){

            //toast indication recherche
            Toast toast = Toast.makeText(mainSearch.this, "recherche...", Toast.LENGTH_LONG);
            toast.show();

            EditText Editsearch = (EditText) findViewById(R.id.editText); //zone de texte éditable pour la recherche

            String Search = Editsearch.getText().toString(); //valeur zone texte


            //link de l'intent/service pour la recherche avec le reciever pour récupérer la réponse
            IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            ResponseReceiver receiver = new ResponseReceiver();
            registerReceiver(receiver, filter);

            //démarage service
            Intent myIntent = new Intent(v.getContext(), MonServiceLourd.class);
            myIntent.putExtra(MonServiceLourd.PARAM_IN_MSG, Search);
            startService(myIntent);


        }
    });
}
//function pour changer les valeurs dans la listview. appelée par le reciver
public void change(String txt, Bitmap b) {

    TextView textView = (TextView) findViewById(R.id.title);
    textView.setText(txt);

    ImageView i = (ImageView) findViewById(R.id.list_image);
    i.setImageBitmap(b);
}



// event sensor changer.
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long currentTime = System.currentTimeMillis();

            //si temps passé est suffisant
            if ((currentTime - lastTime) > 100) {
                long diffTime = (currentTime - lastTime);
                lastTime = currentTime;
                //calcul vitesse
                float speed = Math.abs(x + y + z - lastX - lastY - lastZ)/ diffTime * 10000;

                //si vitesse calculée > à la limite
                if (speed > LimiteSpd) {

                    //appel toast pour faire une petite blague avec la vitesse
                    Toast toast = Toast.makeText(mainSearch.this, "Ne me secouez pas !!", Toast.LENGTH_LONG);
                    toast.show();


                }
                //on garde les valeurs pour le prochain round
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
