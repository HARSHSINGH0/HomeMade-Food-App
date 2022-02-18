package com.vehaas.homemadefood.model;

public class ModelFood {
    private String foodId,foodName,foodDesc,dishStyle,foodQuantity,foodIcon,originalPrice,timing,timestamp,userID;

    public ModelFood() {
    }

    public ModelFood(String foodId, String foodName, String foodDesc, String dishStyle, String foodQuantity, String foodIcon, String originalPrice, String timing, String timestamp, String userID) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.dishStyle = dishStyle;
        this.foodQuantity = foodQuantity;
        this.foodIcon = foodIcon;
        this.originalPrice = originalPrice;
        this.timing = timing;
        this.timestamp = timestamp;
        this.userID = userID;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getDishStyle() {
        return dishStyle;
    }

    public void setDishStyle(String dishStyle) {
        this.dishStyle = dishStyle;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getFoodIcon() {
        return foodIcon;
    }

    public void setFoodIcon(String foodIcon) {
        this.foodIcon = foodIcon;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
