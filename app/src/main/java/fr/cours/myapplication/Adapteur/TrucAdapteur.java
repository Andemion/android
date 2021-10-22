package fr.cours.myapplication.Adapteur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.cours.myapplication.BO.Truc;
import fr.cours.myapplication.R;

public class TrucAdapteur  extends ArrayAdapter{

    private int idPresnetationLigne;

    public TrucAdapteur(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        idPresnetationLigne = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ligneView = inflater.inflate(idPresnetationLigne,parent,false);

        TextView tvNom = ligneView.findViewById(R.id.nom_truc);
        TextView tvValeur = ligneView.findViewById(R.id.valeur_truc);

        Truc truc = (Truc) getItem(position);

        tvNom.setText(truc.getLibelle());
        tvValeur.setText(truc.getValeur());

        return ligneView;
    }
}
