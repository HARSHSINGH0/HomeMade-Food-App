package com.vehaas.homemadefood.model;

public class ModelKitchen {
    private String uid,name,Kitchen_name,kitchen_phone,Kitchen_style,Kitchen_address;

    public ModelKitchen() {
    }

    public ModelKitchen(String uid, String name, String kitchen_name, String kitchen_phone, String kitchen_style, String kitchen_address) {
        this.uid = uid;
        this.name = name;
        Kitchen_name = kitchen_name;
        this.kitchen_phone = kitchen_phone;
        Kitchen_style = kitchen_style;
        Kitchen_address = kitchen_address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

