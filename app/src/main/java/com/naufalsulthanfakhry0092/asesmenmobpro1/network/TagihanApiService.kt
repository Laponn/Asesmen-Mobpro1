package com.naufalsulthanfakhry0092.asesmenmobpro1.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://billbro-api-production.up.railway.app/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface TagihanApiService {
    @GET("api/tagihan")
    suspend fun getTagihan(): String
}

object TagihanApi {
    val service: TagihanApiService by lazy {
        retrofit.create(TagihanApiService::class.java)
    }
}