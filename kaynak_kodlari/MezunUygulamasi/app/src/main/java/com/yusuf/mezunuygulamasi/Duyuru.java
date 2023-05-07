package com.yusuf.mezunuygulamasi;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Duyuru {
    private String baslik, aciklama;
    private int day, month, year;


    public Duyuru() {
    }

    public Duyuru(String baslik, String aciklama, int day, int month, int year) {
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    static public ArrayList<Duyuru> getData() {
        ArrayList<Duyuru> duyurularArray = new ArrayList<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("duyurular");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ;
                    Duyuru duyuru = snapshot.getValue(Duyuru.class);
                    duyurularArray.add(duyuru);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return duyurularArray;
    }
}
