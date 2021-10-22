package fr.cours.myapplication.BO;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import java.net.MalformedURLException;
import java.net.URL;


public class Article implements Parcelable {


    private String nom ;
    private String prix ;
    private String descriptio ;
    private float rating ;
    private String url ;


    private URL adresse;

    {
        try {
            adresse = new URL ("https://www.facebook.com/boulangerievalentinmarnaz/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Article() {
    }

    public Article(String nom, String prix, String descriptio, float rating) {
        this.nom = nom;
        this.prix = prix;
        this.descriptio = descriptio;
        this.rating = rating;
    }


    protected Article(Parcel in) {
        nom = in.readString();
        prix = in.readString();
        descriptio = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getNom() {
        return nom;
    }

    public String getPrix() {
        return prix;
    }

    public String getDescriptio() {
        return descriptio;
    }

    public float getRating() {
        return rating;
    }

    public URL getAdresse() {
        return adresse;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Article{" +
                "nom='" + nom + '\'' +
                ", prix=" + prix +
                ", descriptio='" + descriptio + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prix);
        dest.writeString(descriptio);
        dest.writeFloat(rating);
    }
}
