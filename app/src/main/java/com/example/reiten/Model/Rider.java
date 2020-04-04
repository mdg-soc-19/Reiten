package com.example.reiten.Model;

public class Rider {
    private String bhawan, name, phone;

    public Rider() {
    }

    public Rider(String bhawan, String name, String phone) {
        this.bhawan = bhawan;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getBhawan() {
        return bhawan;
    }

    public void setBhawan(String bhawan) {
        this.bhawan = bhawan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
