package com.android.pc.map.api;

import com.android.pc.map.bean.Gps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GpsPositionApi {

    @POST("request")
    Call<Gps> getGpsPositions();
}
