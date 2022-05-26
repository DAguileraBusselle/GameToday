package com.dam.gametoday.notif;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAeRSS55w:APA91bHYITi_i4HQ77YMbrLsjXzlmruNNc3W1u3knb_yhoghc3XCftr7rMYHlWfBY0SDmPO_awLM0isdlrLVcTAbJI3vJ1DHG_OKPIRBdUk7POQDhNGMIKb-8UniPedny6TLGB3QAw1N" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}