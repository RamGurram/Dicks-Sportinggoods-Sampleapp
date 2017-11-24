package com.androidtask.network;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DSGLabsRestClient {

    private static String BASE_URL = "https://movesync-qa.dcsg.com/dsglabs/mobile/";

    private VenueApi.DSGAPIVenue DSGAPIVenue;

    private Retrofit retrofit;

    private static DSGLabsRestClient instance = null;

    private DSGLabsRestClient() {
        initializeRetrofit();
    }

    public static DSGLabsRestClient getInstance() {
        if (instance == null) {
            instance = new DSGLabsRestClient();
        }
        return instance;
    }

    public VenueApi.DSGAPIVenue getDSGVenusImpl() {
        if (DSGAPIVenue == null) {
            DSGAPIVenue = retrofit.create(VenueApi.DSGAPIVenue.class);
        }
        return DSGAPIVenue;
    }



    private void initializeRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url()
                                .newBuilder()
                                .build();
                        Request.Builder builder = request.newBuilder()
                                .url(url)
                                .method(request.method(), request.body());
                        request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
