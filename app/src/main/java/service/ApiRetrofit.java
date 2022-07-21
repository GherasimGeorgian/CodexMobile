package service;


import android.app.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {
    private static ApiRetrofit instance = null;
    public static final String BASE_URL = "http://192.168.1.37:8080/api/";

    private Retrofit retrofit;

    public static ApiRetrofit getInstance() {
        if (instance == null) {
            instance = new ApiRetrofit();
        }

        return instance;
    }


    private ApiRetrofit() {

        buildRetrofit(BASE_URL);
    }


    private void buildRetrofit(String BASE_URL) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        this.retrofit = builder.build();
    }

    public Retrofit getRetrofit() {
        return this.retrofit;
    }

}