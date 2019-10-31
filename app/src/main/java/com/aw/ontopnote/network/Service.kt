package com.aw.ontopnote.network

import com.aw.ontopnote.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


object Service {
    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.101:1337/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val onTopNoteService : OnTopNoteService by lazy {
        retrofit.create(OnTopNoteService::class.java)
    }

    interface OnTopNoteService {
        @FormUrlEncoded
        @POST("signup")
        fun signUp(@Field("email") email: String, @Field("password") password: String) : Call<User>

        @FormUrlEncoded
        @POST("login")
        fun login(@Field("email") email: String,
                  @Field("password") password: String,
                  @Field("device") device: String,
                  @Field("os") os: String) : Call<User>
    }
}