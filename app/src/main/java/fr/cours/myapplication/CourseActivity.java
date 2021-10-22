package fr.cours.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fr.cours.myapplication.BO.Article;

public class CourseActivity extends AppCompatActivity {

    private Article article = new Article();
    private Button tv_urlArticle;
    private String urlArticle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent intent = getIntent();
        urlArticle = intent.getStringExtra("urlArticle");
        tv_urlArticle = (Button) findViewById(R.id.tv_urlArticle);
        tv_urlArticle.setText(urlArticle);

    }

    public void getLink(View view) {
        urlArticle = article.getUrl() ;
        Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(urlArticle));
        if (link.resolveActivity(getPackageManager())== null){
            Toast.makeText(this, "aucune appli dispo", Toast.LENGTH_SHORT).show();
        }else {
            startActivity(link);
        }
    }
}