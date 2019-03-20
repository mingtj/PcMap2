package com.android.pc.map.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

public interface GpsService {
    @POST("request")
    Call<ResponseBody> GetPositions();
}
