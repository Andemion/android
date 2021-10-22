package fr.cours.myapplication.BO;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 1er instalation dans gradle de room
// utilisation des annotations de room
// puis dans la couche DAO
@Entity
public class Truc {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "libelle")
    private String libelle;

    @ColumnInfo(name = "valeur")
    private String valeur;

    public Truc() {
    }

    public Truc(String libelle, String valeur) {
        this.libelle = libelle;
        this.valeur = valeur;
    }

    public Truc(int id, String libelle, String valeur) {
        this.id = id;
        this.libelle = libelle;
        this.valeur = valeur;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Truc{" +
                "libelle='" + libelle + '\'' +
                ", valeur='" + valeur + '\'' +
                '}';
    }
}
