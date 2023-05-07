package com.yusuf.mezunuygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.DocumentReference;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class KayitOl extends AppCompatActivity {

    private TextView tvFotoSec;
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etAd, etSoyad, etGirisYili, etMezunYili, etEmail, etSifre, etEgitim, etIs, etTelefon;
    private ImageView ivProfilePhoto;
    private Button btnKayitOl;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private Uri selectedImageUri;
    private String ad, soyad, girisYil, mezunYil, sifre, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        ivProfilePhoto = findViewById(R.id.iv_profile_photo);
        ivProfilePhoto.setVisibility(View.INVISIBLE);
        tvFotoSec = findViewById(R.id.tv_photo);
        tvFotoSec.setVisibility(View.INVISIBLE);
        etAd = findViewById(R.id.et_name);
        etSoyad = findViewById(R.id.et_surname);
        etGirisYili = findViewById(R.id.et_entry_year);
        etMezunYili = findViewById(R.id.et_graduation_year);
        etEmail = findViewById(R.id.et_email);
        etSifre = findViewById(R.id.et_password);
        etEgitim = findViewById(R.id.et_egitim);
        etIs = findViewById(R.id.et_guncelIs);
        etTelefon = findViewById(R.id.telefon);
        ivProfilePhoto = findViewById(R.id.iv_profile_photo);
        btnKayitOl = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();


        // Set an onClickListener for the "Select Photo" button

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

        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kayitOl();
            }
        });

    }

    private void kayitOl() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Kayıt olunuyor...");
        mProgressDialog.show();

        String name = etAd.getText().toString().trim();
        String surname = etSoyad.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String sifre = etSifre.getText().toString().trim();
        String entryYear = etGirisYili.getText().toString().trim();
        String graduationYear = etMezunYili.getText().toString().trim();
        String guncelIs = etIs.getText().toString().trim();
        String telefon = etTelefon.getText().toString().trim();
        String egitim = etEgitim.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Lütfen isminizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(surname)) {
            Toast.makeText(this, "Lütfen soyisminizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Lütfen e-posta adresinizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sifre)) {
            Toast.makeText(this, "Lütfen şifrenizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(entryYear)) {
            Toast.makeText(this, "Lütfen giriş yılınızı girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(graduationYear)) {
            Toast.makeText(this, "Lütfen mezuniyet yılınızı girin.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(guncelIs)) {
            Toast.makeText(this, "Lütfen güncel işinizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(telefon)) {
            Toast.makeText(this, "Lütfen telefonunuzu girin.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(egitim)) {
            Toast.makeText(this, "Lütfen eğitiminizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }
/*
        if (selectedImageUri == null) {
            Toast.makeText(this, "Lütfen bir profil fotoğrafı seçin.", Toast.LENGTH_SHORT).show();
            return;
        }
*/
        mAuth.createUserWithEmailAndPassword(email, sifre)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("ad", name);
                            userInfo.put("soyad", surname);
                            userInfo.put("giris_yili", entryYear);
                            userInfo.put("mezun_yili", graduationYear);
                            userInfo.put("email", email);
                            userInfo.put("guncelIs", guncelIs);
                            userInfo.put("egitim", egitim);
                            userInfo.put("telefon", telefon);

                            currentUserDb.setValue(userInfo).addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    //  uploadProfilePhoto(selectedImageUri, FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    mProgressDialog.dismiss();

                                    Toast.makeText(this, "Kayıt işlemi başarıyla tamamlandı.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, GirisActivity.class));
                                    finish();
                                } else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(this, "Kayıt olurken bir hata oluştu, lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(this, "Kayıt olurken bir hata oluştu, lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void kayitOl2() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Kayıt olunuyor...");
        mProgressDialog.show();


        String ad = etAd.getText().toString().trim();
        String soyad = etSoyad.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String sifre = etSifre.getText().toString().trim();
        String entryYear = etGirisYili.getText().toString().trim();
        String graduationYear = etMezunYili.getText().toString().trim();

        if (TextUtils.isEmpty(ad)) {
            Toast.makeText(this, "Lütfen isminizi girin.", Toast.LENGTH_SHORT).show();
            // Toast.makeText(this, "Lütfen isminizi girin.", Toast.LENGTH_SHORT).show();

            return;
        }

        if (TextUtils.isEmpty(soyad)) {
            Toast.makeText(this, "Lütfen soyisminizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Lütfen e-posta adresinizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sifre)) {
            Toast.makeText(this, "Lütfen şifrenizi girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(entryYear)) {
            Toast.makeText(this, "Lütfen giriş yılınızı girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(graduationYear)) {
            Toast.makeText(this, "Lütfen mezuniyet yılınızı girin.", Toast.LENGTH_SHORT).show();
            return;
        }
/*
                if (imageUri == null) {
                    Toast.makeText(this, "Lütfen bir profil fotoğrafı seçin.", Toast.LENGTH_SHORT).show();
                    return;
                }
*/
// Firebase Authentication ile kullanıcı oluşturma
        mAuth.createUserWithEmailAndPassword(email, sifre)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Yeni kullanıcı başarıyla oluşturuldu
                        // Kullanıcının profil bilgilerini Firebase Realtime Database'e kaydetme
                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("ad", ad);
                        userInfo.put("soyad", soyad);
                        userInfo.put("giris_yili", entryYear);
                        userInfo.put("mezun_yili", graduationYear);
                        userInfo.put("email", email);

                        uploadProfilePhoto(selectedImageUri, FirebaseAuth.getInstance().getCurrentUser().getUid());


                        currentUserDb.updateChildren(userInfo).addOnCompleteListener(dbTask -> {
                            if (dbTask.isSuccessful()) {
                                // Kullanıcı profil bilgileri başarıyla kaydedildi
                                Log.d("KayitOl", "Kullanıcı profil bilgileri başarıyla kaydedildi");
                                mProgressDialog.dismiss();
                                Toast.makeText(this, "Kayıt işlemi başarıyla tamamlandı.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, GirisActivity.class));
                                finish();
                            } else {
                                // Kullanıcı profil bilgileri kaydedilemedi
                                mProgressDialog.dismiss();
                                Toast.makeText(this, "Kayıt olurken bir hata oluştu, lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Kullanıcı oluşturma başarısız oldu
                        mProgressDialog.dismiss();
                        Toast.makeText(this, "Kayıt olurken bir hata oluştu, lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
                    }
                });


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
                ivProfilePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}