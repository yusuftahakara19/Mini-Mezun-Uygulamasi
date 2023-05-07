package com.yusuf.mezunuygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class popup_duyuru_ekle extends AppCompatActivity {
    private EditText editTextBaslik;
    private EditText editTextAciklama;
    private DatePicker datePickerSonTarih;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_duyuru_ekle);

        // Firebase veritabanı referansını al
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // EditText ve DatePicker bileşenlerini tanımla
        editTextBaslik = findViewById(R.id.editTextBaslik);
        editTextAciklama = findViewById(R.id.editTextAciklama);
        datePickerSonTarih = findViewById(R.id.datePickerSonTarih);
        // Kaydet butonuna tıklama olayı ekle
        Button btnKaydet = findViewById(R.id.btnKaydet);
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Başlık, açıklama ve tarih değerlerini al
                String baslik = editTextBaslik.getText().toString();
                String aciklama = editTextAciklama.getText().toString();
                int day = datePickerSonTarih.getDayOfMonth();
                int month = datePickerSonTarih.getMonth() + 1;
                int year = datePickerSonTarih.getYear();

                // Firebase veritabanına kaydetmek için rastgele bir anahtar oluştur
                String key = mDatabase.child("duyurular").push().getKey();

                // Duyuru objesi oluştur ve Firebase veritabanına ekle

                Duyuru duyuru = new Duyuru(baslik, aciklama, day, month, year);
                mDatabase.child("duyurular").child(key).setValue(duyuru);
                Toast.makeText(popup_duyuru_ekle.this, "Duyuru kaydedildi.", Toast.LENGTH_SHORT).show();

                // Duyuru oluşturma ekranını kapat ve DuyurularActivity'ye geri dön
                Intent intent = new Intent(popup_duyuru_ekle.this, Duyurular.class);

                startActivity(intent);
                finish();
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(popup_duyuru_ekle.this, Duyurular.class);

        startActivity(intent);
        finish();

    }
}

