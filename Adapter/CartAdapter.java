package my.edu.utar.foodcourtdrinkorderingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import java.util.ArrayList;
import my.edu.utar.foodcourtdrinkorderingapp.Domain.Drinks;
import my.edu.utar.foodcourtdrinkorderingapp.Helper.ChangeNumberItemsListener;
import my.edu.utar.foodcourtdrinkorderingapp.Helper.ManagementCart;
import my.edu.utar.foodcourtdrinkorderingapp.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    private ArrayList<Drinks> list;
    private ManagementCart managementCart;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private String userId;

    public CartAdapter(ArrayList<Drinks> list, Context context, ChangeNumberItemsListener changeNumberItemsListener, String userId) {
        this.list = list;
        this.userId = userId;
        this.managementCart = new ManagementCart(context, userId);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        Drinks drink = list.get(position);
        holder.title.setText(drink.getTitle());
        holder.feeEachItem.setText(String.format("$%.2f", drink.getNumberInCart() * drink.getPrice()));
        holder.totalEachItem.setText(drink.getNumberInCart() + " * $" + (drink.getPrice()));
        holder.num.setText(String.valueOf(drink.getNumberInCart()));
        holder.iceAmount.setText("Ice: " + drink.getIceAmount());
        holder.sweetnessLevel.setText("Sweetness: " + drink.getSweetnessLevel());

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(v -> managementCart.plusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

        holder.minusItem.setOnClickListener(v -> managementCart.minusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem, iceAmount, sweetnessLevel;
        ImageView pic;
        TextView totalEachItem, num;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberItemTxt);
            iceAmount = itemView.findViewById(R.id.iceAmountTxt);
            sweetnessLevel = itemView.findViewById(R.id.sweetnessTxt);
        }
    }
}
