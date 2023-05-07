package com.yusuf.mezunuygulamasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Duyurular extends AppCompatActivity {

    private RecyclerView dRecyclerView;
    private Button btnDuyuruOlustur;
    private DuyurularAdapter adapter;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duyurular);
        editText=(EditText)findViewById(R.id.editTextDuyuru) ;
        dRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewDuyurular);
        adapter = new DuyurularAdapter(Duyuru.getData(), this);
        dRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dRecyclerView.setLayoutManager(manager);
        dRecyclerView.setAdapter(adapter);
        btnDuyuruOlustur = (Button) findViewById(R.id.btnDuyuruOlustur);
        editText.requestFocus();
        btnDuyuruOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Duyurular.this, popup_duyuru_ekle.class);
                //intent.putExtra("id", userId); // User nesnesini GirisActivity'e gönder
                startActivity(intent);
                finish();
            }
        });
    }
}