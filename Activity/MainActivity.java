package my.edu.utar.foodcourtdrinkorderingapp.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import my.edu.utar.foodcourtdrinkorderingapp.Adapter.BestDrinksAdapter;
import my.edu.utar.foodcourtdrinkorderingapp.Adapter.CategoryAdapter;
import my.edu.utar.foodcourtdrinkorderingapp.Domain.Category;
import my.edu.utar.foodcourtdrinkorderingapp.Domain.Drinks;
import my.edu.utar.foodcourtdrinkorderingapp.Domain.Location;
import my.edu.utar.foodcourtdrinkorderingapp.Domain.Price;
import my.edu.utar.foodcourtdrinkorderingapp.Domain.Time;
import my.edu.utar.foodcourtdrinkorderingapp.R;
import my.edu.utar.foodcourtdrinkorderingapp.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        displayUserName();
        initLocation();
        initTime();
        initPrice();
        initBestDrink();
        initCategory();
        setVariable();
    }

    private void displayUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.child("Name").getValue(String.class);
                        if (userName != null) {
                            binding.textUser.setText(userName);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    private void setVariable() {
        binding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        });
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=binding.searchEdt.getText().toString();
                if(!text.isEmpty()){
                    Intent intent=new Intent(MainActivity.this, ListDrinksActivity.class);
                    intent.putExtra("text",text);
                    intent.putExtra("isSearch",true );
                    startActivity(intent);

                }
            }
        });

        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,CartActivity.class)));
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String userName = snapshot.child("Name").getValue(String.class);
                                String userEmail = snapshot.child("Email").getValue(String.class);
                                String userPhoneNo = snapshot.child("PhoneNo").getValue(String.class);

                                // Start ProfileActivity and pass user data as extras
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                intent.putExtra("Name", userName);
                                intent.putExtra("Email", userEmail);
                                intent.putExtra("PhoneNo", userPhoneNo);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }
        });

    }

    private void initBestDrink() {
        //The path of Drinks can change to drink after all picture and naming change
        DatabaseReference myRef= database.getReference("Drinks");
        binding.progressBarBestDrink.setVisibility(View.VISIBLE);
        ArrayList<Drinks> list=new ArrayList<>();
        Query query=myRef.orderByChild("BestDrink").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Drinks.class));
                    }
                    if (list.size() >0){
                        binding.bestDrinkView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        RecyclerView.Adapter adapter=new BestDrinksAdapter(list, userName);
                        binding.bestDrinkView.setAdapter(adapter);
                    }
                    binding.progressBarBestDrink.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initCategory() {

        DatabaseReference myRef= database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list=new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Category.class));
                    }
                    if (list.size() >0){
                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this,4));
                        RecyclerView.Adapter adapter=new CategoryAdapter(list);
                        binding.categoryView.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initLocation() {
        DatabaseReference myRef=database.getReference("Location");
        ArrayList<Location> list =new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter= new ArrayAdapter<>(MainActivity.this, R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initTime() {
        DatabaseReference myRef=database.getReference("Time");
        ArrayList<Time> list =new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Time.class));
                    }
                    ArrayAdapter<Time> adapter= new ArrayAdapter<>(MainActivity.this, R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.timeSp.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initPrice() {
        DatabaseReference myRef=database.getReference("Price");
        ArrayList<Price> list =new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Price.class));
                    }
                    ArrayAdapter<Price> adapter= new ArrayAdapter<>(MainActivity.this, R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.priceSp.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}