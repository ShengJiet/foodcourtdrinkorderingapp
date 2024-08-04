package my.edu.utar.foodcourtdrinkorderingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import my.edu.utar.foodcourtdrinkorderingapp.R;

public class SignupActivity extends AppCompatActivity {
    EditText signupName,signupEmail,signupPhoneNo,signupPassword;
    TextView loginRedirectText;
    Button signupBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        signupName=findViewById(R.id.nameEdt);
        signupEmail=findViewById(R.id.emailEdt);
        signupPhoneNo=findViewById(R.id.phoneEdt);
        signupPassword=findViewById(R.id.passEdt);
        signupBtn=findViewById(R.id.signupBtn);
        loginRedirectText=findViewById(R.id.loginRedirectText);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name=signupName.getText().toString();
                String Email=signupEmail.getText().toString();
                String PhoneNo=signupPhoneNo.getText().toString();
                String Password=signupPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(SignupActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignupActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();

                                // Proceed with storing additional user data in the Realtime Database
                                HashMap<String, Object> userData = new HashMap<>();
                                userData.put("Name", Name);
                                userData.put("Email", Email);
                                userData.put("PhoneNo", PhoneNo);

                                FirebaseDatabase.getInstance().getReference("User").child(user.getUid()).setValue(userData);

                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Registration failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
