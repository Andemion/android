package fr.cours.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.cours.myapplication.BO.Truc;

// utilisation de room pour le BDD dans une interface
// puis dans AppDataBase
@Dao
public interface TrucDAORoom {

    @Query("SELECT * FROM Truc")
    List<Truc> getAll();

    @Query("SELECT * FROM Truc WHERE id IN (:ids)")
    List<Truc> loadSeveralByIDs (int[] ids);

    @Query("SELECT * FROM Truc WHERE libelle LIKE :libelle")
    Truc findByLibelle(String libelle);

    @Insert
    void insert(Truc... trucs);

    @Delete
    void delete(Truc truc);

}
