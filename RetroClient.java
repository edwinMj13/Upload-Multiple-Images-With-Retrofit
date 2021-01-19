package com.example.imageuploading__retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static Retrofit retrofit=null;
    private static final String BASE_URL="http://192.168.0.102/thanova/";
    public static Retrofit getRetrofit(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
            //    .connectTimeout(10, TimeUnit.SECONDS)
              //  .readTimeout(10,TimeUnit.SECONDS)
                // .writeTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}
