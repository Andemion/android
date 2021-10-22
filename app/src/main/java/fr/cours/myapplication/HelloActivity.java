package fr.cours.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HelloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);


    }

    public void clickResult(View view) {

        Intent resultat = new Intent();
        resultat.putExtra("info", "you going to died");
        this.setResult(RESULT_OK,resultat);
        finish();
    }
}