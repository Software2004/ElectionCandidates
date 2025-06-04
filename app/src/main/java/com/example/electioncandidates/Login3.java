package com.example.electioncandidates;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login3 extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login3);
        findViewById(R.id.signin).setOnClickListener(view -> startActivity(new Intent(Login3.this, Signup.class)));

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.btnShowSet);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            return;
        }

        ProgressDialog progDialog = new ProgressDialog(this);
        progDialog.setMessage("Please Wait....");
        progDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        if (user.isEmailVerified())
                            updateUI(user);
                        else{
                            Toast.makeText(this, "Please verify your email address.", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }
                    }
                    else
                        Toast.makeText(Login3.this, "Authentication Fialed!", Toast.LENGTH_SHORT).show();
                    progDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                        progDialog.dismiss();
                        Toast.makeText(Login3.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Navigate to the main activity
            startActivity(new Intent(Login3.this, MainActivity.class));
            finish();
        }
    }
}