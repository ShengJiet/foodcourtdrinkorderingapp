package my.edu.utar.foodcourtdrinkorderingapp.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.foodcourtdrinkorderingapp.R;

public class ProfileActivity extends AppCompatActivity {
    TextView profileName, profileEmail, profilePhoneNo;
    TextView titleName, titlePhoneNo;
    Button editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhoneNo = findViewById(R.id.profilePhoneno);
        titleName = findViewById(R.id.titleName);
        titlePhoneNo = findViewById(R.id.titlePhoneNo);
        editProfile = findViewById(R.id.editButton);

        // Retrieve user data from extras
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("Name");
        String emailUser = intent.getStringExtra("Email");
        String phonenoUser = intent.getStringExtra("PhoneNo");

        // Display user data
        titleName.setText(nameUser);
        titlePhoneNo.setText(phonenoUser);
        profileName.setText(nameUser);
        profileEmail.setText(emailUser);
        profilePhoneNo.setText(phonenoUser);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle edit profile button click
                passUserData();
            }
        });
    }

    public void showAllUserData(){
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("Name");
        String emailUser = intent.getStringExtra("Email");
        String phonenoUser = intent.getStringExtra("PhoneNo");
        titleName.setText(nameUser);
        titlePhoneNo.setText(phonenoUser);
        profileName.setText(nameUser);
        profileEmail.setText(emailUser);
        profilePhoneNo.setText(phonenoUser);
    }
    public void passUserData(){
        String email = profileEmail.getText().toString().trim();
        // Sanitize the email address
        String sanitizedEmail = email.replace(".", "_dot_").replace("#", "_hash_")
                .replace("$", "_dollar_").replace("[", "_leftBracket_")
                .replace("]", "_rightBracket_");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUserDatabase = reference.orderByChild("Email").equalTo(email);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // Retrieve user data using the sanitized email as the key
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String nameFromDB = dataSnapshot.child("Name").getValue(String.class);
                        String emailFromDB = dataSnapshot.child("Email").getValue(String.class);
                        String phonenoFromDB = dataSnapshot.child("PhoneNo").getValue(String.class);
                        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                        intent.putExtra("Name", nameFromDB);
                        intent.putExtra("Email", emailFromDB);
                        intent.putExtra("PhoneNo", phonenoFromDB);
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

}