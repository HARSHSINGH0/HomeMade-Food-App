package com.vehaas.homemadefood.model;

public class ModelKitchen {
    private String kitchen_desc;
    private String kitchen_userId,Kitchen_name,kitchen_phone,Kitchen_style,Kitchen_address;

    public ModelKitchen() {
    }



    public ModelKitchen(String kitchen_desc, String kitchen_userId, String kitchen_name, String kitchen_phone, String kitchen_style, String kitchen_address) {
        this.kitchen_desc = kitchen_desc;
        this.kitchen_userId = kitchen_userId;
        this.Kitchen_name = kitchen_name;
        this.kitchen_phone = kitchen_phone;
        this.Kitchen_style = kitchen_style;
        this.Kitchen_address = kitchen_address;
    }

    public String getKitchen_desc() {
        return kitchen_desc;
    }

    public void setKitchen_desc(String kitchen_desc) {
        this.kitchen_desc = kitchen_desc;
    }

    public String getKitchen_userId() {
        return kitchen_userId;
    }

    public void setKitchen_userId(String kitchen_userId) {
        this.kitchen_userId = kitchen_userId;
    }

    public String getKitchen_name() {
        return Kitchen_name;
    }

    public void setKitchen_name(String kitchen_name) {
        Kitchen_name = kitchen_name;
    }

    public String getKitchen_phone() {
        return kitchen_phone;
    }

    public void setKitchen_phone(String kitchen_phone) {
        this.kitchen_phone = kitchen_phone;
    }

    public String getKitchen_style() {
        return Kitchen_style;
    }

    public void setKitchen_style(String kitchen_style) {
        Kitchen_style = kitchen_style;
    }

    public String getKitchen_address() {
        return Kitchen_address;
    }

    public void setKitchen_address(String kitchen_address) {
        Kitchen_address = kitchen_address;
    }
}

