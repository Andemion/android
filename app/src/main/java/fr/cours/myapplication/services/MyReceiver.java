package fr.cours.myapplication.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// le broadcast permet l'excution d'une action selon evenement
// n'utilisa pas d'IHM
// ce joue dans le thread principal
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}