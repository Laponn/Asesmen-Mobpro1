package com.naufalsulthanfakhry0092.asesmenmobpro1.network

import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://billbro-api-production.up.railway.app/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TagihanApiService {
    @GET("api/tagihan")
    suspend fun getTagihan(): List<Tagihan>
}

object TagihanApi {
    val service: TagihanApiService by lazy {
        retrofit.create(TagihanApiService::class.java)
    }

    fun getImageUrl(imageId: String): String {
        return "${BASE_URL}images/$imageId.jpg"
    }
}

