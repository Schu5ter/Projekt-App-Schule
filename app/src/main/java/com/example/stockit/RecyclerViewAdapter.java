package com.example.stockit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";


    private ArrayList<Artikel> mArtikelnamen ;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<Artikel> mArtikelnamen, Context mContext) {
        this.mArtikelnamen = mArtikelnamen;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        //Firebase daten

         Artikel artikel = mArtikelnamen.get(position);

        holder.artikelname.setText(artikel.getArtikelname());
        holder.artikelanzahl.setText((artikel.getNumber()));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"funktioniert" , Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return mArtikelnamen.size();
    }










    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView artikelname;
        TextView artikelanzahl;
        ImageView balken;
        Button delete;
        RelativeLayout parent_layout;

        public ViewHolder( View itemView) {
            super(itemView);
            balken = itemView.findViewById(R.id.balken);
            artikelanzahl = itemView.findViewById(R.id.artikelanzahl);
            artikelname = itemView.findViewById(R.id.artikelname);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            delete = itemView.findViewById(R.id.delete);



        }
    }

}
