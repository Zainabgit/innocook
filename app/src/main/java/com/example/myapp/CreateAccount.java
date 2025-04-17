package com.example.myapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapp.Util.PasswordUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateAccount extends Activity {

    EditText accountFirstName, accountLastName, accountEmail, accountPassword, confirmPassword;
    Button submitButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_pg);

        dialog = new ProgressDialog(CreateAccount.this);
        dialog.setTitle("Loading....");

        accountFirstName = findViewById(R.id.textFirstName);
        accountLastName = findViewById(R.id.textLastName);
        accountEmail = findViewById(R.id.textEmail);
        accountPassword = findViewById(R.id.textPassword);
        confirmPassword = findViewById(R.id.textConfirmPassword);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from EditText fields
                String firstName = accountFirstName.getText().toString().trim();
                String lastName = accountLastName.getText().toString().trim();
                String email = accountEmail.getText().toString().trim();
                String password = accountPassword.getText().toString().trim();
                String confirmPasswordText = confirmPassword.getText().toString().trim();

                // Check if any field is empty
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPasswordText.isEmpty()) {
                    // Highlight empty fields
                    if (firstName.isEmpty()) {
                        accountFirstName.setError("First name cannot be empty");
                    }
                    if (lastName.isEmpty()) {
                        accountLastName.setError("Last name cannot be empty");
                    }
                    if (email.isEmpty()) {
                        accountEmail.setError("Email cannot be empty");
                    }
                    if (password.isEmpty()) {
                        accountPassword.setError("Password cannot be empty");
                    }
                    if (confirmPasswordText.isEmpty()) {
                        confirmPassword.setError("Confirm password cannot be empty");
                    }
                    // Display error message
                    //Toast.makeText(CreateAccount.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Invalid email format
                    accountEmail.setError("Invalid email format");
                    // Display error message
                    //Toast.makeText(CreateAccount.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else if (!isStrongPassword(password)) {
                    // Weak password
                    accountPassword.setError("Password should be at least 8 characters long and contain uppercase, lowercase, and numbers");
                    // Display error message
                    //Toast.makeText(CreateAccount.this, "Weak password", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPasswordText)) {
                    // Password and confirm password do not match
                    accountPassword.setError("Passwords do not match");
                    confirmPassword.setError("Passwords do not match");
                    // Display error message
                    //Toast.makeText(CreateAccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(accountFirstName.getWindowToken(), 0);

                    // All fields are filled and passwords match, proceed with database operation
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference().child("Users");

                    // Create HelperClass object with hashed password
                    String hashedPassword = PasswordUtils.hashPassword(password);

                    // Create HelperClass object
                    HelperClass helperClass = new HelperClass(firstName, lastName, email, hashedPassword,null);

                    String key = encodeEmail(email);
                    // Set value in database
                    reference.child(key).setValue(helperClass);

                    // Display success message
                    dialog.hide();
                    Toast toast = Toast.makeText(CreateAccount.this, "You have Created Your Account Successfully!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    Intent intent = new Intent(CreateAccount.this, AccountActivity.class);
                    startActivity(intent);
                }
            }
        });



        ImageView imageViewBackArrow2 = findViewById(R.id.imageViewBackArrow2); // Find the ImageView

        imageViewBackArrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this,AccountActivity.class);
                startActivity(intent);
            }
        });

    }


    private String encodeEmail(String email) {
        return email.replace(".", "_"); // Replace periods with underscores
    }

    // Function to check if password is strong
    private boolean isStrongPassword(String password) {
        // Define criteria for strong password
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        // Check if password matches criteria
        return password.matches(passwordPattern);
    }
}

