package my.edu.utar.foodcourtdrinkorderingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.foodcourtdrinkorderingapp.R;

public class LoginActivity extends AppCompatActivity {
    EditText emailEdt, passEdt;
    Button loginBtn;
    TextView signupRedirectText, forgetPasswordText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEdt = findViewById(R.id.emailEdt);
        passEdt = findViewById(R.id.passEdt);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginBtn = findViewById(R.id.loginBtn);
        forgetPasswordText = findViewById(R.id.forgetPasswordText);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdt.getText().toString().trim();
                String password = passEdt.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the email exists in the User table in the Firebase Realtime Database
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
                    userRef.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email exists in the User table, proceed with signing in
                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Exception exception = task.getException();
                                                if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                                    // Wrong password for the provided email
                                                    Toast.makeText(LoginActivity.this, "The password inserted is wrong", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Other authentication failures
                                                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Email does not exist in the User table
                                Toast.makeText(LoginActivity.this, "The email hasn't been registered yet", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled
                        }
                    });
                }
            }
        });


        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdt.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your email first", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the email exists in the User table in the Firebase Realtime Database
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
                    userRef.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email exists in the User table, proceed with sending password reset email
                                mAuth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // Email does not exist in the User table
                                Toast.makeText(LoginActivity.this, "Please sign up with this email first", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled
                        }
                    });
                }
            }
        });

    }
}
