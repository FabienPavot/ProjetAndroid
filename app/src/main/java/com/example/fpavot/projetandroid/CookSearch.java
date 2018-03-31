package com.example.fpavot.projetandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CookSearch extends AppCompatActivity { //Java pour la première activité

    @Override
    //page d'accueil
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_search);

//gestion du bouton pour aller vers l'autre activité
        Button yourButton = (Button) findViewById(R.id.button);

        yourButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //démarage de la 2e activité
                startActivity(new Intent(CookSearch.this, mainSearch.class));
            }
        });
    }


}
