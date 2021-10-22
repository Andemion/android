package fr.cours.myapplication.dao;

import android.content.Context;

import androidx.room.Room;

public class ConnectionRoom {

    public static AppDataBase getConnexion(Context context){

        return Room.databaseBuilder(context,AppDataBase.class,"TrucBDD").build();
    }
}
// puis dans une activity dans onResume
