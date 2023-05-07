package com.yusuf.mezunuygulamasi;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String ad;
    private String soyad;
    private String email;
    private String giris_yili;
    private String mezun_yili;
    private String guncelIs;
    private String telefon;
    private String egitim;

    public User() {
        // no-argument constructor required by Firebase Realtime Database
    }

    public User(String ad, String soyad, String email, String giris_yili, String mezun_yili,String guncelIs,String telefon,String egitim) {
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.giris_yili = giris_yili;
        this.mezun_yili = mezun_yili;
        this.guncelIs = guncelIs;
        this.egitim = egitim;
        this.telefon = telefon;

    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGiris_yili() {
        return giris_yili;
    }

    public void setGiris_yili(String giris_yili) {
        this.giris_yili = giris_yili;
    }


    public String getGuncelIs() {
        return guncelIs;
    }

    public void setGuncelIs(String guncelIs) {
        this.guncelIs = guncelIs;
    }


    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    public String getEgitim() {
        return egitim;
    }

    public void setEgitim(String egitim) {
        this.egitim = egitim;
    }
    public String getMezun_yili() {
        return mezun_yili;
    }

    public void setMezun_yili(String mezun_yili) {
        this.mezun_yili = mezun_yili;
    }


    static public ArrayList<User> getData(){
        ArrayList<User> usersArray = new ArrayList<User>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                      usersArray.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return usersArray;
    }
}

