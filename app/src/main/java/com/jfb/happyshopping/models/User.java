package com.jfb.happyshopping.models;

import java.util.ArrayList;

public class User {
    private String email;
    private String _id;
    private ArrayList<String> lists;

    public ArrayList<String> getLists() {
        return lists;
    }

    public void setLists(ArrayList<String> lists) {
        this.lists = lists;
    }

    public User(String email, String _id, ArrayList<String> lists) {

        this.email = email;
        this._id = _id;
        this.lists = lists;
    }

    public User(String _id, String email) {
        this._id = _id;
        this.email = email;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
