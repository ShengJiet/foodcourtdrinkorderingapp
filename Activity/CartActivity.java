package my.edu.utar.foodcourtdrinkorderingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import my.edu.utar.foodcourtdrinkorderingapp.Adapter.CartAdapter;
import my.edu.utar.foodcourtdrinkorderingapp.Domain.Drinks;
import my.edu.utar.foodcourtdrinkorderingapp.Helper.ManagementCart;
import my.edu.utar.foodcourtdrinkorderingapp.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;

    private RecyclerView.Adapter adapter;
    private ManagementCart managementCart;
    private String userId;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve userId from intent
        userId = getIntent().getStringExtra("USER_ID");

        // Initialize ManagementCart with userId
        managementCart = new ManagementCart(this, userId);

        setVariable();
        calculateCart();
        initlist();

        binding.button2.setOnClickListener(v -> placeOrder());
    }

    private void initlist() {
        if (managementCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);

        // Pass userId to CartAdapter
        adapter = new CartAdapter(managementCart.getListCart(), this, () -> calculateCart(), userId);
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.04; // 4% tax
        double delivery = 0; // RM 0 for delivery

        tax = managementCart.getTotalFee() * percentTax * 100.00 / 100;
        double total = (managementCart.getTotalFee() + tax + delivery) * 100 / 100;
        double itemTotal = managementCart.getTotalFee() * 100/ 100;

        binding.totalFeeTxt.setText(String.format("$%.2f", itemTotal));
        binding.taxTxt.setText(String.format("$%.2f", tax));
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText(String.format("$%.2f", total));
    }

    private void setVariable() {

        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void placeOrder() {
        String tableNumber = binding.editTextText.getText().toString().trim();
        if (tableNumber.isEmpty()) {
            // Handle case where table number is not provided
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String orderId = databaseReference.child("Order").push().getKey();
        String billId = databaseReference.child("Bill").push().getKey();
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Create order and bill data
        Map<String, Object> orderData = new HashMap<>();
        Map<String, Object> billData = new HashMap<>();

        orderData.put("tableNumber", tableNumber);
        orderData.put("dateTime", currentDateTime);

        billData.put("tableNumber", tableNumber);
        billData.put("dateTime", currentDateTime);
        billData.put("tax", tax);
        billData.put("totalPrice", binding.totalTxt.getText().toString());

        // Insert the order data
        if (orderId != null) {
            databaseReference.child("Order").child(orderId).setValue(orderData);
        }

        // Insert the bill data
        if (billId != null) {
            databaseReference.child("Bill").child(billId).setValue(billData);
        }

        // Insert each item in the order
        for (Drinks drink : managementCart.getListCart()) {
            if (orderId != null) {
                String itemId = databaseReference.child("Order").child(orderId).push().getKey();

                Map<String, Object> itemData = new HashMap<>();
                itemData.put("itemName", drink.getTitle());
                itemData.put("quantity", drink.getNumberInCart());
                itemData.put("iceAmount", drink.getIceAmount());
                itemData.put("sweetnessLevel", drink.getSweetnessLevel());

                if (itemId != null) {
                    databaseReference.child("Order").child(orderId).child(itemId).setValue(itemData);
                }
            }

            if (billId != null) {
                String billItemId = databaseReference.child("Bill").child(billId).push().getKey();

                Map<String, Object> billItemData = new HashMap<>();
                billItemData.put("itemName", drink.getTitle());
                billItemData.put("quantity", drink.getNumberInCart());
                billItemData.put("price", drink.getPrice());
                billItemData.put("subtotal", drink.getNumberInCart() * drink.getPrice());

                if (billItemId != null) {
                    databaseReference.child("Bill").child(billId).child(billItemId).setValue(billItemData);
                }
            }
        }

        // Clear the cart after placing the order
        managementCart.clearCart();
        finish();
    }

}
