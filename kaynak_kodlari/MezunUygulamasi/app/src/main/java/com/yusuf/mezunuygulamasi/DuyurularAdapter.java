package com.yusuf.mezunuygulamasi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DuyurularAdapter extends RecyclerView.Adapter<DuyurularAdapter.DuyurularHolder> {
    private ArrayList<Duyuru> duyuruList;
    private Context context;

    public DuyurularAdapter(ArrayList<Duyuru> duyuruList, Context context) {
        this.duyuruList = duyuruList;
        this.context = context;
    }

    @NonNull
    @Override
    public DuyurularHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.duyurular_item, parent, false);


        return new DuyurularHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DuyurularHolder holder, int position) {
        Duyuru duyuru = duyuruList.get(position);
        holder.setData(duyuru);
    }

    @Override
    public int getItemCount() {
        return duyuruList.size();
    }

    class DuyurularHolder extends RecyclerView.ViewHolder {

        TextView duyuruBaslik, duyuruAciklama, duyuruTarih;


        public DuyurularHolder(@NonNull View itemView) {
            super(itemView);
            duyuruBaslik = (TextView) itemView.findViewById(R.id.duyuralar_item_baslik);
            duyuruAciklama = (TextView) itemView.findViewById(R.id.duyurular_item_aciklama);
            duyuruTarih = (TextView) itemView.findViewById(R.id.duyuru_item_sonTarih);


        }

        public void setData(Duyuru duyuru) {
            String tarih = String.valueOf("Tarih : "+duyuru.getDay()) + "." + String.valueOf(duyuru.getMonth()) + "." + String.valueOf(duyuru.getYear());
            this.duyuruBaslik.setText(duyuru.getBaslik());
            this.duyuruAciklama.setText(duyuru.getAciklama());
            this.duyuruTarih.setText(tarih);
        }
    }


}
