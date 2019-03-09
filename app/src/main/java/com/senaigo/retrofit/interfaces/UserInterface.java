package com.senaigo.retrofit.interfaces;

import com.senaigo.retrofit.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserInterface {
    @GET("posts")
    Call<List<User>> get();

    @POST("posts")
    Call<User> post(@Body User user);

    @PUT("posts")
    Call<User> put(@Body User user);

    @DELETE("posts")
    Call<Void> delete(@Body User user);
}
