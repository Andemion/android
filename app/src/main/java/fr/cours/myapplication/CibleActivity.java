package fr.cours.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class CibleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cible);

        SharedPreferences spInter = getSharedPreferences(MainActivity.NOM_FICHIER,MODE_PRIVATE);
        String interValue = spInter.getString(MainActivity.CLE_INTER,"vide");
        TextView tvInter = findViewById(R.id.tv_inter_value);
        tvInter.setText(interValue);


    }
}