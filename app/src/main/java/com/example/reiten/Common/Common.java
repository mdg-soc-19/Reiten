package com.example.reiten.Common;

import com.example.reiten.Remote.IGoogleAPI;
import com.example.reiten.Remote.RetrofitClient;

public class Common {
    public static final String baseURL="https://maps.googleapis.com";
    public static IGoogleAPI getGoogleAPI(){
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);

    }
}
