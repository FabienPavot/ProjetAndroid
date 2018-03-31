package com.example.fpavot.projetandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;


//recepteur réponse du service pour la recherche
public class ResponseReceiver extends BroadcastReceiver {
    public static final String ACTION_RESP =
            "com.intent.action.MESSAGE_PROCESSED";

    @Override
    public void onReceive(Context context, Intent intent) {

        //debug
        System.out.println( "recived !!!!!!!!!!!!!!!");
        System.out.println( " recived : " + intent.getStringExtra(MonServiceLourd.PARAM_OUT_MSG));
        // </debug>

        //on récupère les réponses via intent et on appelle change.
        String tText = intent.getStringExtra(MonServiceLourd.PARAM_OUT_MSG);
        mainSearch c = (mainSearch) context;
        c.change(tText,(Bitmap) intent.getParcelableExtra("BitmapImage"));
    }
}
