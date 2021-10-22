package fr.cours.myapplication.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fr.cours.myapplication.BO.Truc;

// room vas créé la BDD il faut mettre toutes les entity
// pour update il sufit de changé de version
// abstract obligatoire
// dans connectionRoom
@Database(entities = {Truc.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TrucDAORoom trucDAORoom();
}
