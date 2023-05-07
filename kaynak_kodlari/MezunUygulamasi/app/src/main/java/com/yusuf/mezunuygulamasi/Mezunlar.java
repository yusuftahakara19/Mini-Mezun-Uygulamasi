package com.yusuf.mezunuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Mezunlar extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private EditText editText;
    private MezunlarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mezunlar);
        editText = (EditText) findViewById(R.id.editTextSearch);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMezunlar);
        adapter = new MezunlarAdapter(User.getData(), this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        editText.requestFocus();
        /*
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // burada metin değişmeden önce yapılacak işlemler yazılır
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("çalıştı");
                String searchText = s.toString().toLowerCase();
                ArrayList<User> filteredList = new ArrayList<>();
                ArrayList<User> array = new ArrayList<>();

                array = getArray();

                filteredList.addAll(array);

                MezunlarAdapter newAdapter = new MezunlarAdapter(filteredList, Mezunlar.this);
                mRecyclerView.setAdapter(newAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // burada metin değiştikten sonra yapılacak işlemler yazılır
            }
        });*/
    }

    public ArrayList<User> getArray() {
        ArrayList<User> usersArray = new ArrayList<User>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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