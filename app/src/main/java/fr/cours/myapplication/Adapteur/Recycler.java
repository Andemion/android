package fr.cours.myapplication.Adapteur;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.PublicKey;
import java.util.ArrayList;

import fr.cours.myapplication.BO.Article;
import fr.cours.myapplication.R;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    private ArrayList<Article> articles;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNom;
        public TextView tvPrix;
        public RatingBar rbRating;
        public TextView tvDescription;


        public ViewHolder(@NonNull View v) {
            super(v);
            tvNom = v.findViewById(R.id.tv_nomArticle);
            tvPrix = v.findViewById(R.id.tv_prixArticle);
            rbRating = v.findViewById(R.id.rb_ratingArticle);
            tvDescription = v.findViewById(R.id.tv_description);
        }
    }

    public Recycler(ArrayList<Article> myDataset) {
        articles = myDataset;
    }

    @NonNull
    @Override
    public Recycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_liste,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler.ViewHolder holder, int position) {

        Article art = articles.get(position);
        holder.tvNom.setText(art.getNom());
        holder.tvPrix.setText(art.getPrix());
        holder.rbRating.setRating(art.getRating());
        holder.tvDescription.setText(art.getDescriptio());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }



}
