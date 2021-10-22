package fr.cours.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fr.cours.myapplication.BO.Truc;
import fr.cours.myapplication.contracts.TrucContract;

// ici utilisation de sqlite pour la BDD
public class TrucDao {

    private SQLiteDatabase db;
    private BddHelper helper;

    public TrucDao(Context context){
        helper = new BddHelper(context);
        db = helper.getWritableDatabase();
    }

    public long insert(Truc truc){

        ContentValues cv = new ContentValues();
        cv.put(TrucContract.COL_LIBELLE,truc.getLibelle());
        cv.put(TrucContract.COL_VALEUR,truc.getValeur());

        return db.insert(TrucContract.TABLE_NAME,null,cv);
    }

    public Truc get(int id){

        Truc truc = null;

        Cursor cursor = db.query(
                TrucContract.TABLE_NAME,
                new String[]{TrucContract.COL_ID,TrucContract.COL_LIBELLE,TrucContract.COL_VALEUR},
                TrucContract.COL_ID+ "= ?",
                new String[]{String.valueOf(id)},
                null,null,null,null
        );
        if (cursor.moveToNext()){
           truc = new Truc();
           truc.setId(cursor.getInt(cursor.getColumnIndex(TrucContract.COL_ID)));
           truc.setLibelle(cursor.getString(cursor.getColumnIndex(TrucContract.COL_LIBELLE)));
           truc.setValeur(cursor.getString(cursor.getColumnIndex(TrucContract.COL_VALEUR)));
        }

        return truc;
    }


    public List<Truc> get(){

        List<Truc> resultat = new ArrayList<>();

        Cursor cursor = db.query(
                TrucContract.TABLE_NAME,
                new String[]{TrucContract.COL_ID,TrucContract.COL_LIBELLE,TrucContract.COL_VALEUR},
                null, null, null,null,null,null
        );

        while (cursor.moveToNext()){
            Truc truc = new Truc();
            truc.setId(cursor.getInt(cursor.getColumnIndex(TrucContract.COL_ID)));
            truc.setLibelle(cursor.getString(cursor.getColumnIndex(TrucContract.COL_LIBELLE)));
            truc.setValeur(cursor.getString(cursor.getColumnIndex(TrucContract.COL_VALEUR)));
            resultat.add(truc);
        }

        return resultat;
    }

    public boolean update(Truc truc){

        ContentValues cv = new ContentValues();
        cv.put(TrucContract.COL_LIBELLE,truc.getLibelle());
        cv.put(TrucContract.COL_VALEUR,truc.getValeur());

        return db.update(
                TrucContract.TABLE_NAME,
                cv,
                TrucContract.COL_ID+"=?",
                new String[]{String.valueOf(truc.getId())}
        )>0;
    }

    public boolean delete(int id){

        return db.delete(
                TrucContract.TABLE_NAME,
                TrucContract.COL_ID+"=?",
                new String[]{String.valueOf(id)}
        )>0;
    }
}
