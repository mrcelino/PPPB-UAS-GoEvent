package com.example.uaspppb.network

import com.example.uaspppb.model.Event
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("acara")
    fun getAllEvents(): Call<List<Event>>

    @POST("acara")
    fun addEvent(@Body requestBody: RequestBody): Call<Event>


    @POST("acara/{id}")
    fun updateEvent(@Path("id") eventId: String, @Body event: Event): Call<Event>


    @DELETE("acara/{id}")
    fun deleteEvent(@Path("id") id: String): Call<Unit>
}
