package com.example.myapp;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.Session.SessionManager;
import com.example.myapp.Util.PasswordUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    //Variables
    EditText signinEmail, signinPassword;

    Button SignInBtn;

    ProgressDialog dialog;


    // Get Data Variables
    TextInputLayout email;
    TextInputLayout password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_pg);

        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setTitle("Loading....");

        //Hooks for animation
        SignInBtn = findViewById(R.id.submit);

        //Hooks for getting data
        signinEmail = findViewById(R.id.textEmail);
        signinPassword = findViewById(R.id.textPassword);

        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail()){
                    checkUser();
                }
            }
        });

    }


    //Validate Functions

    private boolean validateEmail() {
        signinEmail = findViewById(R.id.textEmail);
        String val = signinEmail.getText().toString().trim();

        if (val.isEmpty()){
            signinEmail.setError("Field cannot be empty");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            signinEmail.setError("Invalid Email!");
            return false;
        }else {
            signinEmail.setError(null);
            return true;
        }
    }

    public void checkUser(){
        dialog.show();
        String userEmail = signinEmail.getText().toString().trim();
        String userPassword = signinPassword.getText().toString().trim();
        String key = encodeEmail(userEmail);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUserDatabase = reference.orderByKey().equalTo(key);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.hide();
                if (snapshot.exists()) {
                    signinEmail.setError(null);
                    // Since there might be multiple nodes with the same key, we iterate over them
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String storedHash = childSnapshot.child("password").getValue(String.class);
                        HelperClass retrievedUser = childSnapshot.getValue(HelperClass.class);
                         if (PasswordUtils.checkPassword(userPassword, storedHash)) {
                            signinPassword.setError(null);
                            // Save user details in session
                            SessionManager sessionManager = new SessionManager(SignInActivity.this);
                            sessionManager.saveUser(retrievedUser);
                            Intent intent = new Intent(SignInActivity.this, CameraActivity.class);
                            startActivity(intent);
                        }
                        else {
                            showToast("Invalid credentials");
                            return;
                        }
                    }
                } else {
                    showToast("Invalid credentials");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.hide();
            }
        });

    }

    // Method to show a toast message
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show());
    }
    private String encodeEmail(String email) {
        return email.replace(".", "_"); // Replace periods with underscores
    }
}
