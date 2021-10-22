package fr.cours.myapplication.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import fr.cours.myapplication.contracts.TrucContract;

public class BddHelper extends SQLiteOpenHelper {

    public BddHelper(@Nullable Context context) {
        super(context, "MaBdd.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TrucContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
