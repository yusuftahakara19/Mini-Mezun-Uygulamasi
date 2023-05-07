package com.yusuf.mezunuygulamasi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MezunlarAdapter extends RecyclerView.Adapter<MezunlarAdapter.mezunlarHolder> {

    private ArrayList<User> mezunlarList;
    private ArrayList<User> filteredList;
    private Context context;
    private static final String TAG = "MyActivity";
    private String userId;


    public MezunlarAdapter(ArrayList<User> mezunlarList, Context context) {
        this.filteredList =mezunlarList;
        this.mezunlarList = mezunlarList;
        this.context = context;
    }





    @NonNull
    @Override
    public mezunlarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mezunlar_item, parent, false);
        return new mezunlarHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mezunlarHolder holder, int position) {


        User user = mezunlarList.get(position);
        holder.setData(user);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = user.getEmail();



                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Kullanıcı Bilgileri");
                    builder.setMessage(
                            "İsim: " + user.getAd() + " " + user.getSoyad() + "\n" +
                            "Eğitim : " + user.getEgitim() + "\n" +
                            "Güncel İş : " + user.getGuncelIs() + "\n" +
                            "Telefon : " + user.getTelefon() + "\n" +
                            "E-posta: " + user.getEmail() + "\n" +
                            "Dönem: (" + user.getGiris_yili() + " - " + user.getMezun_yili() + ")");
                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                builder.setNegativeButton("Mail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + user.getEmail()));
                        context.startActivity(intent);
                    }
                });
                builder.setNeutralButton("WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phoneNumberWithCountryCode = "+90" + user.getTelefon(); // replace with your country code and phone number
                        String message = "Merhaba "+user.getAd()+" "+user.getSoyad(); // replace with your message
                        String url = "https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + message;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        context.startActivity(intent);
                    }
                });
                    AlertDialog dialog = builder.create();
                    dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mezunlarList.size();
    }

    public void filter(List<User> userList) {
        mezunlarList.clear();
        mezunlarList.addAll(userList);
        filteredList.clear();
        filteredList.addAll(userList);
        notifyDataSetChanged();
    }


    class mezunlarHolder extends RecyclerView.ViewHolder {
        TextView mezunIsmi, mezunEposta, mezunDonem;
        ImageView mezunResim;

        public mezunlarHolder(@NonNull View itemView) {
            super(itemView);
            mezunIsmi = (TextView) itemView.findViewById(R.id.mezunlar_item_mezunIsim);
            mezunEposta = (TextView) itemView.findViewById(R.id.mezunlar_item_mezunEposta);
            mezunResim = (ImageView) itemView.findViewById(R.id.mezunlar_item_imageView);
            mezunDonem = (TextView) itemView.findViewById(R.id.mezunlar_item_mezunDonem);

        }



String userId;
        public void setData(User user) {
            this.mezunIsmi.setText(user.getAd()+" "+ user.getSoyad());
            this.mezunDonem.setText("Dönem : ("+user.getGiris_yili()+" - "+user.getMezun_yili()+")");
            this.mezunEposta.setText(user.getEmail());

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

            Query query = usersRef.orderByChild("email").equalTo(user.getEmail());

            final String[] userId = new String[1];

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            userId[0] = snapshot.getKey();

                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference photoRef = storageRef.child("profile_photos/"+userId[0]);

// Fotoğrafı indirin ve ImageView'a yükleyin
                            photoRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    mezunResim.setImageBitmap(bitmap);
                                    try {
                                        // Kodunuz burada
                                        Thread.sleep(100); // Kodun 3 saniye boyunca durması
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Hata durumunda yapılacak işlemleri burada belirtebilirsiniz
                                }
                            });
                            Log.d("User ID", userId[0]);

                        }
                    } else {
                        Log.d("User ID", "User not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Error", databaseError.getMessage());
                }
            });





        }




    }
}
