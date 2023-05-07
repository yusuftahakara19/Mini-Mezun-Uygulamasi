package com.yusuf.mezunuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class GirisActivity extends AppCompatActivity {

    private TextView adSoyadTextView;
    private ImageView imageView;

    private Button btnMezunlar,btnDuyurular,btnBilgileriGuncelle;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        imageView = (ImageView)findViewById(R.id.profile_photo_iv);
        adSoyadTextView = (TextView)findViewById(R.id.user_name_tv);
        btnMezunlar = (Button) findViewById(R.id.mezunlar_btn);
        btnDuyurular = (Button) findViewById(R.id.duyurular_btn);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        btnBilgileriGuncelle = findViewById(R.id.guncelle_btn);
        btnBilgileriGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisActivity.this, Guncelle_Sil.class);
                startActivity(intent);
                finish();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Kullanıcının adı ve soyadını dataSnapshot'den alıp textview'e yazdırın
                String ad = dataSnapshot.child("ad").getValue(String.class);
                String soyad = dataSnapshot.child("soyad").getValue(String.class);
                adSoyadTextView.setText("Hoşgeldin "+ad + " " + soyad);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hata durumunda yapılacaklar
            }
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference photoRef = storageRef.child("profile_photos/" + id);

// Fotoğrafı indirin ve ImageView'a yükleyin
        photoRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Hata durumunda yapılacak işlemleri burada belirtebilirsiniz
            }
        });
        btnMezunlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GirisActivity.this, Mezunlar.class);
                startActivity(intent);
            }
        });

        btnDuyurular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GirisActivity.this, Duyurular.class);
                startActivity(intent);


            }
        });


    }
}
