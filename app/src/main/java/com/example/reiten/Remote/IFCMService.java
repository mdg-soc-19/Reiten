package com.example.reiten.Remote;

import com.example.reiten.Model.FCMResponse;
import com.example.reiten.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAApihWgCE:APA91bEcdPGIt9tBsuix4Q4w7EcOlTIylXubpHgNhrQ3qTQwUlstdtNi6LUOyMonp_bQmJQ-kToAxQhAO5YvYCVcPsQtITiJmSiCbA_fsjIo38APnEKcd1iHS5LCeWzDkciR-Re3xuZC"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
