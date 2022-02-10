package com.vehaas.homemadefood;

public class Kitchen_Newid_helper {
    String Kitchen_name,Kitchen_desc,Kitchen_style,Kitchen_userId, Kitchen_address,Kitchen_phone;

    public Kitchen_Newid_helper(String kitchen_name, String kitchen_desc, String kitchen_style,String kitchen_address,String kitchen_phone, String kitchen_userId) {
        Kitchen_name = kitchen_name;
        Kitchen_desc = kitchen_desc;
        Kitchen_style = kitchen_style;
        Kitchen_userId = kitchen_userId;
        Kitchen_address = kitchen_address;
        Kitchen_phone = kitchen_phone;

    }
    public Kitchen_Newid_helper() {
    }
    public String getKitchen_phone() {
        return Kitchen_phone;
    }

    public void setKitchen_phone(String kitchen_phone) {
        this.Kitchen_phone = kitchen_phone;
    }


    public String getKitchen_address() {
        return Kitchen_address;
    }

    public void setKitchen_address(String kitchen_address) {
        Kitchen_address = kitchen_address;
    }

    public String getKitchen_name() {
        return Kitchen_name;
    }

    public void setKitchen_name(String kitchen_name) {
        Kitchen_name = kitchen_name;
    }

    public String getKitchen_desc() {
        return Kitchen_desc;
    }

    public void setKitchen_desc(String kitchen_desc) {
        Kitchen_desc = kitchen_desc;
    }

    public String getKitchen_style() {
        return Kitchen_style;
    }

    public void setKitchen_style(String kitchen_style) {
        Kitchen_style = kitchen_style;
    }

    public String getKitchen_userId() {
        return Kitchen_userId;
    }

    public void setKitchen_userId(String kitchen_userId) {
        Kitchen_userId = kitchen_userId;
    }
}
