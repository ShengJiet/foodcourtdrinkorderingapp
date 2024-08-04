package my.edu.utar.foodcourtdrinkorderingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import my.edu.utar.foodcourtdrinkorderingapp.R;

public class EditProfileActivity extends AppCompatActivity {
    EditText editName, editEmail, editPhoneNo;
    Button saveButton;
    String nameUser, emailUser, phonenoUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("User");
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhoneNo = findViewById(R.id.editPhoneNo);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve user data from intent extras
        Intent intent = getIntent();
        nameUser = intent.getStringExtra("Name");
        emailUser = intent.getStringExtra("Email");
        phonenoUser = intent.getStringExtra("PhoneNo");

        // Set initial text of EditText fields
        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editPhoneNo.setText(phonenoUser);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle save button click
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        // Retrieve edited text from EditText fields
        String editedName = editName.getText().toString();
        String editedEmail = editEmail.getText().toString();
        String editedPhoneNo = editPhoneNo.getText().toString();

        if (editedEmail != null && !editedEmail.isEmpty()) {
            // Sanitize the email address
            String sanitizedEmail = sanitizeEmail(editedEmail);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
            Query checkUserDatabase = reference.orderByChild("Email").equalTo(editedEmail);
            checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            // Get the key of the user node
                            String userKey = userSnapshot.getKey();

                            // Update the user details
                            reference.child(userKey).child("Name").setValue(editedName);
                            reference.child(userKey).child("Email").setValue(editedEmail);
                            reference.child(userKey).child("PhoneNo").setValue(editedPhoneNo);

                            // Show a toast message indicating successful save
                            Toast.makeText(EditProfileActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();

                            // Exit the loop since we found the user
                            break;
                        }
                    } else {
                        // Handle the case where the user does not exist
                        Toast.makeText(EditProfileActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle any errors
                    Toast.makeText(EditProfileActivity.this, "Failed to save changes: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle the case where editedEmail is null or empty
            Toast.makeText(EditProfileActivity.this, "Failed to save changes. Email is null or empty.", Toast.LENGTH_SHORT).show();
        }
    }



    private String sanitizeEmail(String email) {
        // Sanitize the email address
        // Replace characters not allowed in Firebase Database paths
        if (email != null && !email.isEmpty()) {
            return email.replace(".", "_dot_")
                    .replace("#", "_hash_")
                    .replace("$", "_dollar_")
                    .replace("[", "_leftBracket_")
                    .replace("]", "_rightBracket_");
        } else {
            return null;
        }
    }



}