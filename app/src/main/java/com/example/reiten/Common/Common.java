package com.example.reiten.Common;

import android.location.Location;

import com.example.reiten.Remote.FCMClient;
import com.example.reiten.Remote.IFCMService;
import com.example.reiten.Remote.IGoogleAPI;
import com.example.reiten.Remote.RetrofitClient;


public class Common {

    public static final String driver_tbl ="Drivers";
    public static final String user_driver_tbl ="DriversInformation";
    public static final String user_rider_tbl ="RidersInformation";
    public static final String pickup_request_tbl ="PickupRequest";
    public static final String token_tbl ="Tokens";

    public static Location mLastLocation = null;


    public static final String baseURL = "https://maps.googleapis.com";
    public static final String fcmURL = "https://fcm.googleapis.com/";

    public static IGoogleAPI getGoogleAPI(){
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);

    }
    public static IFCMService getFCMservice(){
        return FCMClient.getClient(fcmURL).create(IFCMService.class);

    }
}
