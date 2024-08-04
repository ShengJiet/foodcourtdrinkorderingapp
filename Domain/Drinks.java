package my.edu.utar.foodcourtdrinkorderingapp.Domain;

import java.io.Serializable;

public class Drinks implements Serializable {
    private int CategoryId;
    private String Description;
    private  boolean BestDrink;
    private int Id;
    private int LocationId;
    private double Price;
    private String  ImagePath;
    private  int PriceId;
    private double Star;
    private int TimeId;
    private int TimeValue;
    private String Title;
    private int numberInCart;
    private String iceAmount = "Normal";  // Default value
    private String sweetnessLevel = "Median";
    public Drinks() {
    }

    @Override
    public String toString() {
        return Title;
    }

    public String getIceAmount() {
        return iceAmount;
    }

    public void setIceAmount(String iceAmount) {
        this.iceAmount = iceAmount;
    }

    public String getSweetnessLevel() {
        return sweetnessLevel;
    }

    public void setSweetnessLevel(String sweetnessLevel) {
        this.sweetnessLevel = sweetnessLevel;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isBestDrink() {
        return BestDrink;
    }

    public void setBestDrink(boolean bestDrink) {
        BestDrink = bestDrink;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getLocationId() {
        return LocationId;
    }

    public void setLocationId(int locationId) {
        LocationId = locationId;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getPriceId() {
        return PriceId;
    }

    public void setPriceId(int priceId) {
        PriceId = priceId;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public int getTimeId() {
        return TimeId;
    }

    public void setTimeId(int timeId) {
        TimeId = timeId;
    }

    public int getTimeValue() {
        return TimeValue;
    }

    public void setTimeValue(int timeValue) {
        TimeValue = timeValue;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
