package com.jfb.happyshopping.models;

public class Friend {

    private String userID;
    private String name;
    private boolean isAdded;

    public Friend() {
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Friend(String userID, String name) {

        this.userID = userID;
        this.name = name;
    }
}
