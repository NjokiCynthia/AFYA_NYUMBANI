package com.example.sasa_doc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    Button register;
    EditText Name, Phone, Age, Email, password;
    TextView banner;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        Name = findViewById(R.id.Name);
        Phone = findViewById(R.id.Phone);
        Age = findViewById(R.id.Age);
        Email = findViewById(R.id.Email);
        password = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                register();
                break;
        }
    }

    public void register() {
        String email = Email.getText().toString().trim();
        String phone = Phone.getText().toString().trim();
        String name = Name.getText().toString().trim();
        String age = Age.getText().toString().trim();
        String Password = password.getText().toString().trim();


        register = findViewById(R.id.register);
        register.setOnClickListener((View.OnClickListener) view -> {
            Intent intent = new Intent(Register.this, com.example.sasa_doc.MainActivity.class);
            startActivity(intent);

        });

        if (name.isEmpty()) {
            Name.setError("Full Name is required!");
            Name.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            Age.setError("Age is required!");
            Age.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            Phone.setError("Phone number is required!");
            Phone.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Email.setError("Email is required!");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please provide a valid email.");
            Email.requestFocus();
            return;

        }
        if (Password.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            password.setError("Password length should be six characters");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                User user = new User(name, phone, age, email);

                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(Register.this, "User has been registered successfully.",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.VISIBLE);
                    } else
                        Toast.makeText(Register.this, "Failed to register user! Try again!.",
                                Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                });
            } else
                Toast.makeText(Register.this, "Failed to register user! Try again!.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);


        });
    }
}