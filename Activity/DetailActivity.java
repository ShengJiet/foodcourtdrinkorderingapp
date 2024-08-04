package my.edu.utar.foodcourtdrinkorderingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import my.edu.utar.foodcourtdrinkorderingapp.Domain.Drinks;
import my.edu.utar.foodcourtdrinkorderingapp.Helper.ManagementCart;
import my.edu.utar.foodcourtdrinkorderingapp.R;
import my.edu.utar.foodcourtdrinkorderingapp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private Drinks object;
    private int num=1;
    private String userId;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        userId = getIntent().getStringExtra("USER_ID");
        managementCart=new ManagementCart(this,userId);
        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceTxt.setText("$"+object.getPrice());
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText((num*object.getPrice()+ "$"));

        binding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=num+1;
                binding.numTxt.setText(num+" ");
                binding.totalTxt.setText("$"+(num* object.getPrice()));
            }
        });

        binding.minusBtn.setOnClickListener(v -> {
            if(num>1){
                num=num-1;
                binding.numTxt.setText(num+"");
                binding.totalTxt.setText("$"+(num*object.getPrice()));
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            object.setIceAmount(getSelectedIceAmount());
            object.setSweetnessLevel(getSelectedSweetnessLevel());
            managementCart.insertDrink(object);
        });
    }

    private String getSelectedIceAmount() {
        int selectedId = binding.iceOptions.getCheckedRadioButtonId();
        if (selectedId == R.id.iceNone) return "None";
        if (selectedId == R.id.iceLittle) return "Half";
        if (selectedId == R.id.iceNormal) return "Normal";
        return "Extra";
    }

    private String getSelectedSweetnessLevel() {
        int selectedId = binding.sweetnessOptions.getCheckedRadioButtonId();
        if (selectedId == R.id.sweetnessNone) return "None";
        if (selectedId == R.id.sweetnessLow) return "Low";
        if (selectedId == R.id.sweetnessMedium) return "Medium";
        return "High";
    }

    private void getIntentExtra() {
        object=(Drinks) getIntent().getSerializableExtra("object");
    }
}