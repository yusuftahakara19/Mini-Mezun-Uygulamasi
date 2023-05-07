package com.yusuf.mezunuygulamasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Guncelle_Sil extends AppCompatActivity {
    private Button btnGuncelle,btnSil,btnSifreGuncelle;
    private static final String TAG = "MyActivity";
    private ProgressDialog mProgressDialog;
    private TextView tvFotoSec;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private  ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guncelle_sil);
        TextView eposta = (TextView)findViewById(R.id.get_email);
        eposta.setEnabled(false);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnGuncelle = (Button)findViewById(R.id.btn_guncelle);
        btnSil = (Button)findViewById(R.id.btn_sil);
        btnSifreGuncelle = (Button)findViewById(R.id.btn_SifreGuncelle);

        tvFotoSec = findViewById(R.id.gtv_photo);
        imageView = (ImageView)findViewById(R.id.giv_profile_photo) ;
        tvFotoSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send a request to open the image gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }

        });
        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Guncelle_Sil.this)
                        .setTitle("Kaydı Sil")
                        .setMessage("Kaydınızı Silmek istiyorsunuz, emin misiniz? Bunun geri dönüşü yoktur.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();


// Kaydı silmek için referansı al
                                DatabaseReference ref = database.getReference("Users").child(uid);

// Veriyi sil
                                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Guncelle_Sil.this, "Kayıt silindi.", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(Guncelle_Sil.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Guncelle_Sil.this, "Kayıt silinemedi, tekrar deneyiniz.", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Guncelle_Sil.this, "Kayıt silinemedi, tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                                        // Burada yapılacak işlemler
                                    }
                                });

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        btnSifreGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String newPassword = ((EditText) findViewById(R.id.get_new_password)).getText().toString().trim();
                String oldPassword = ((EditText) findViewById(R.id.get_password)).getText().toString().trim();;

                FirebaseUser user = mAuth.getCurrentUser();
                if (!TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(oldPassword)){
                    if (user != null) {
                        // Kullanıcı oturum açmışsa, şifre değiştirme işlemi yapabiliriz.
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Guncelle_Sil.this, "Şifre güncellendi.", Toast.LENGTH_SHORT).show();

                                                Log.d(TAG, "Şifre güncellendi.");
                                                // Başarılı bir şekilde şifre değiştirildi
                                            } else {
                                                Toast.makeText(Guncelle_Sil.this, "Şifre güncelleme hatası: ", Toast.LENGTH_SHORT).show();

                                                Log.e(TAG, "Şifre güncelleme hatası: " + task.getException().getMessage());
                                                // Şifre değiştirme hatası oluştu
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(Guncelle_Sil.this, "Kimlik doğrulama hatası: ", Toast.LENGTH_SHORT).show();

                                    Log.e(TAG, "Kimlik doğrulama hatası, eski şifrenizi doğru girdiğinizden emin olun " + task.getException().getMessage());
                                    // Kimlik doğrulama hatası oluştu
                                }
                            }
                        });
                    }
                }else{
                    Toast.makeText(Guncelle_Sil.this, "Şifre bilgilerinizi giriniz!", Toast.LENGTH_SHORT).show();

                }


            }
        });
        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Güncellenecek değerleri al
                String ad = ((EditText) findViewById(R.id.get_name)).getText().toString().trim();
                String soyad = ((EditText) findViewById(R.id.get_surname)).getText().toString().trim();
                String egitim = ((EditText) findViewById(R.id.get_egitim)).getText().toString().trim();
                String guncelIs = ((EditText) findViewById(R.id.get_guncelIs)).getText().toString().trim();
                String telefon = ((EditText) findViewById(R.id.gtelefon)).getText().toString().trim();
                String mezunYili = ((EditText) findViewById(R.id.get_graduation_year)).getText().toString().trim();
                String girisYili = ((EditText) findViewById(R.id.get_entry_year)).getText().toString().trim();

// Güncellenecek verileri bir Map yapısına aktar
                Map<String, Object> updates = new HashMap<>();
                updates.put("ad", ad);
                updates.put("soyad", soyad);
                updates.put("egitim", egitim);
                updates.put("guncelIs", guncelIs);
                updates.put("telefon", telefon);
                updates.put("mezun_yili", mezunYili);
                updates.put("giris_yili", girisYili);

// Veritabanındaki kaydı güncelle
                databaseRef.child("Users").child(uid).updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Guncelle_Sil.this, "Bilgileriniz güncellendi!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Guncelle_Sil.this, "Bilgileriniz güncellenirken hata oluştu!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Bilgiler güncellenirken hata oluştu: " + e.getMessage());
                    }
                });

            }
        });


        databaseRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Verileri al
                String egitim = dataSnapshot.child("egitim").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String girisYili = dataSnapshot.child("giris_yili").getValue(String.class);
                String guncelIs = dataSnapshot.child("guncelIs").getValue(String.class);
                String mezunYili = dataSnapshot.child("mezun_yili").getValue(String.class);
                String soyad = dataSnapshot.child("soyad").getValue(String.class);
                String ad = dataSnapshot.child("ad").getValue(String.class);
                String telefon = dataSnapshot.child("telefon").getValue(String.class);

                // TextView'lere yaz
                ImageView imageView = findViewById(R.id.giv_profile_photo);
                TextView adTextView = findViewById(R.id.get_name);
                adTextView.setText(ad);

                TextView egitimTextView = findViewById(R.id.get_egitim);
                egitimTextView.setText(egitim);

                TextView emailTextView = findViewById(R.id.get_email);
                emailTextView.setText(email);

                TextView girisYiliTextView = findViewById(R.id.get_entry_year);
                girisYiliTextView.setText(girisYili);

                TextView guncelIsTextView = findViewById(R.id.get_guncelIs);
                guncelIsTextView.setText(guncelIs);

                TextView mezunYiliTextView = findViewById(R.id.get_graduation_year);
                mezunYiliTextView.setText(mezunYili);

                TextView soyadTextView = findViewById(R.id.get_surname);
                soyadTextView.setText(soyad);

                TextView telefonTextView = findViewById(R.id.gtelefon);
                telefonTextView.setText(telefon);

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference photoRef = storageRef.child("profile_photos/"+uid);

// Fotoğrafı indirin ve ImageView'a yükleyin
                photoRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(bitmap);
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hata durumu
            }
        });





    }

    public void onBackPressed() {
        Intent intent = new Intent(Guncelle_Sil.this, GirisActivity.class);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        intent.putExtra("id", userId); // User nesnesini GirisActivity'e gönder
        startActivity(intent);
        finish();
    }

    private void uploadProfilePhoto(Uri photoUri, String userId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("profile_photos").child(userId);
        storageReference.putFile(photoUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Photo upload successful, get download URL
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String photoUrl = uri.toString();
                        // Update user's profile photo in Firestore
                        updateUserProfilePhoto(userId, photoUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Photo upload failed
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Fotoğraf yüklenemedi: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
    private void updateUserProfilePhoto(String userId, String photoUrl) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users")
                .document(userId);
        userRef.update("photoUrl", photoUrl)
                .addOnSuccessListener(aVoid -> {
                    // Profile photo URL updated successfully
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Kayıt başarılı", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Profile photo URL update failed
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Kayıt başarısız: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    // Fotoğraf seçildikten sonra yapılacak işlemler
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Eğer request code fotoğraf seçmek için kullanılan PICK_IMAGE_REQUEST ise ve sonuç OK ise
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Seçilen fotoğrafın uri'sini al
            selectedImageUri = data.getData();


            try {
                // Uri'den bitmap oluştur
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                // Bitmap'i image view'da göster
                imageView.setImageBitmap(bitmap);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference photoRef = storageRef.child("profile_photos/"+uid);
/*
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(selectedImageUri !=null)
                            System.out.println(selectedImageUri);
                        uploadProfilePhoto(selectedImageUri, FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Toast.makeText(Guncelle_Sil.this, "Fotoğraf güncellendi.", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Dosya silinemedi, hata mesajını yazdır
                        Toast.makeText(Guncelle_Sil.this, "Fotoğraf güncellenemedi.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Fotoğraf güncelleme hatası: " + e.getMessage());

                    }
                });
*/
                uploadProfilePhoto(selectedImageUri, FirebaseAuth.getInstance().getCurrentUser().getUid());
                Toast.makeText(Guncelle_Sil.this, "Fotoğraf güncellendi.", Toast.LENGTH_SHORT).show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}