package com.example.android.shopinggururegister.Application;

/**
 * Created by Android on 17-12-2016.
 */
public class ShopingGuruRegisterSingleton {

    private String id = "";
    private static ShopingGuruRegisterSingleton instance;

    private ShopingGuruRegisterSingleton() {
    }

    public static ShopingGuruRegisterSingleton getInstance() {
        if (instance == null) {
            instance = new ShopingGuruRegisterSingleton();
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
