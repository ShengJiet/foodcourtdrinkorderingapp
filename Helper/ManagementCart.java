package my.edu.utar.foodcourtdrinkorderingapp.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import my.edu.utar.foodcourtdrinkorderingapp.Domain.Drinks;

public class ManagementCart {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private String userId;

    public ManagementCart(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        sharedPreferences = context.getSharedPreferences("cart_" + userId, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();  // Initialize the editor here
        gson = new Gson();
    }

    public ArrayList<Drinks> getListCart() {
        String json = sharedPreferences.getString(userId, null);
        Type type = new TypeToken<ArrayList<Drinks>>() {}.getType();
        return gson.fromJson(json, type) == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    public void insertDrink(Drinks drink) {
        ArrayList<Drinks> listCart = getListCart();
        listCart.add(drink);
        String json = gson.toJson(listCart);
        editor.putString(userId, json);
        editor.commit();  // Use commit to save the changes
    }

    public void plusNumberItem(ArrayList<Drinks> list, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        list.get(position).setNumberInCart(list.get(position).getNumberInCart() + 1);
        String json = gson.toJson(list);
        editor.putString(userId, json);
        editor.commit();
        changeNumberItemsListener.change();
    }

    public void minusNumberItem(ArrayList<Drinks> list, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (list.get(position).getNumberInCart() > 1) {
            list.get(position).setNumberInCart(list.get(position).getNumberInCart() - 1);
        } else {
            // Remove the item from the list
            list.remove(position);
        }
            String json = gson.toJson(list);
            editor.putString(userId, json);
            editor.commit();
            changeNumberItemsListener.change();
    }


    public double getTotalFee() {
        ArrayList<Drinks> listCart = getListCart();
        double fee = 0;
        for (Drinks drinks : listCart) {
            fee += drinks.getNumberInCart() * drinks.getPrice();
        }
        return fee;
    }

    public void clearCart() {
        editor.remove(userId);
        editor.commit();
    }
}
