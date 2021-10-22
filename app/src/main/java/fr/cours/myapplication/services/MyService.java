package fr.cours.myapplication.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Date;

// fonctionne sur le thread principal si traitement long utilisé un thread independant
public class MyService extends Service {

    public MyService() {
    }

    @Override
    // appeller uniquement au 1er appel
    public void onCreate() {
        super.onCreate();

    }

    @Override
    // appeller a chaque appel du service unidirectionel
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    // fonction qui communique de facon bidirectionnelle et qui permet de ce connecté au service
    public IBinder onBind(Intent intent) {

        return new MonBinder();
    }// retourne un objet IBinder qui permet utilisé le service

    @Override
    // lors que l'on veux ce deconnecté du service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MonBinder extends Binder{

        public Service getServices(){
            return MyService.this;
        }
    }

    public String serviceSurMesure(){

        return "Timestamp : " + new Date().getTime();
    }
}