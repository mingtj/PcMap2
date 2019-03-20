package com.android.pc.map.utils;

import com.android.pc.map.Constants;
import com.android.pc.map.api.GpsService;
import com.android.pc.map.api.SecurityKeyService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HttpUtils {

    private void testRetrofitPost2(){

        //构建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrlTest)//BaseUrlTest
                .build();
        //构建请求
        SecurityKeyService service = retrofit.create(SecurityKeyService.class);
        //对发送请求进行封装
        Call<ResponseBody> positions = service.getSecurityKey();

        //发送异步请求
        positions.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //请求成功时的回调 输出结果-response.body().show();
                try {
                    String r = response.body().string();
                    System.out.println("********%%%%%%******post gps success  result****************:" + r);
                }catch(Exception e){
                    System.out.println("--error---");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //请求失败时候的回调
                System.out.println("*******%%%%%%%*********post gps result fail********************");
            }
        });

    }

}
