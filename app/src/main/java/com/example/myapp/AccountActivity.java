package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_pg);

        Button createAccountButton = findViewById(R.id.create_account_button);
        Button signInButton = findViewById(R.id.sign_in_button);

        createAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

            }
        }





