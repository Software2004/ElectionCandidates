package com.example.electioncandidates;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    private EditText emailEditText, passwordEditText,passwordConfirm;
    private Button signupButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViewById(R.id.back).setOnClickListener(view -> finish());
        findViewById(R.id.signin).setOnClickListener(view -> finish());
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        signupButton = findViewById(R.id.btnShowSet);

        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordConf = passwordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(passwordConf)) {
            passwordConfirm.setError("Password does not match.");
            return;
        }


        ProgressDialog progDialog = new ProgressDialog(this);
        progDialog.setMessage("Please Wait....");
        progDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        progDialog.dismiss();
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(Signup.this, "Verification email sent. Please check your email.", Toast.LENGTH_LONG).show();
                                            updateUI();
                                        } else {
                                            Toast.makeText(Signup.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        progDialog.dismiss();
                        Toast.makeText(Signup.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI() {
        // Navigate to login screen
        startActivity(new Intent(Signup.this, Login3.class));
        finish();
    }
}
