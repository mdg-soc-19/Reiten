package com.example.reiten.Model;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name, phone,rickshaw_no,image;
    @PropertyName("Rickshaw_No")
    public String getRickshaw_no() {
        return rickshaw_no;
    }
    @PropertyName("Rickshaw_No")
    public void setRickshaw_no(String rickshaw_no) {
        this.rickshaw_no = rickshaw_no;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User() {


    }

    public User(String name, String phone, String rickshaw_no, String image) {
        this.name = name;
        this.phone = phone;
        this.rickshaw_no = rickshaw_no;
        this.image = image;
    }
    @PropertyName("Name")
    public String getName() {
        return name;
    }
    @PropertyName("Name")
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", name);
        result.put("Rickshaw_No", rickshaw_no);
        result.put("ImageUri", image);
        return result;
    }
}
