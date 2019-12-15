package com.example.zisakuziten;

import android.database.Observable;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GroupService {
    @GET("groups/?format=json")
    Call<List<Group>> getJson();

    @POST("groups/?format=json")
    @FormUrlEncoded
//    @Headers("Content-Type:application/json")
    Call<Group> saveGPost(@Field("title") String title,
                          @Field("updateTime") String updateTime);

    @POST("ziten/?format=json")
    @FormUrlEncoded
    Call<Ziten> saveZPost(@Field("title") String title,
                          @Field("content") String content,
                          @Field("updateTime") String updateTime,
                          @Field("group") Integer group);
}