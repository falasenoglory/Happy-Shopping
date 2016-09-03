package com.jfb.happyshopping.Models;

/**
 * Created by Shanyl Jimenez on 9/3/2016.
 */
public class Item {

    private String ItemID;
    private String ItemName;
    private int Quantity;
    private Boolean isBought;

    public Item() {
    }

    public Item(String itemID, String itemName, int quantity, Boolean isBought) {
        ItemID = itemID;
        ItemName = itemName;
        Quantity = quantity;
        this.isBought = isBought;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
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
}
