package com.jfb.happyshopping.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shanyl Jimenez on 9/3/2016.
 */
public class Product {

    private String ItemName;
    private int Quantity;
    private Boolean isBought;

    public Product() {
    }

    public Product(String itemName, int quantity, Boolean isBought) {

        ItemName = itemName;
        Quantity = quantity;
        this.isBought = isBought;
    }


    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public Boolean getBought() {
        return isBought;
    }

    public void setBought(Boolean bought) {
        isBought = bought;
    }
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("ItemName",ItemName);
        result.put("Quantity",Quantity);
        result.put("isBought",isBought);
        return result;

    }
}
