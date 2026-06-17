package com.naufalsulthanfakhry0092.asesmenmobpro1.network

import com.naufalsulthanfakhry0092.asesmenmobpro1.model.OpStatus
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

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
    suspend fun getTagihan(@Header("Authorization") userId: String): List<Tagihan>

    @Multipart
    @POST("api/tagihan")
    suspend fun postTagihan(
        @Header("Authorization") userId: String,
        @Part("namaTagihan") nama: RequestBody,
        @Part("totalTagihan") total: RequestBody,
        @Part("jumlahOrang") jumlahOrang: RequestBody,
        @Part("pakaiPajak") pakaiPajak: RequestBody,
        @Part("persentasePajak") persentasePajak: RequestBody,
        @Part("hasilPerOrang") hasilPerOrang: RequestBody,
        @Part("tanggalDibuat") tanggalDibuat: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @Multipart
    @POST("api/tagihan/{id}")
    suspend fun updateTagihan(
        @Header("Authorization") userId: String,
        @Path("id") id: Long,
        @Part("_method") method: RequestBody,
        @Part("namaTagihan") nama: RequestBody,
        @Part("totalTagihan") total: RequestBody,
        @Part("jumlahOrang") jumlahOrang: RequestBody,
        @Part("pakaiPajak") pakaiPajak: RequestBody,
        @Part("persentasePajak") persentasePajak: RequestBody,
        @Part("hasilPerOrang") hasilPerOrang: RequestBody,
        @Part image: MultipartBody.Part?
    ): OpStatus

    @PUT("api/tagihan/soft-delete/{id}")
    suspend fun softDeleteTagihan(
        @Header("Authorization") userId: String,
        @Path("id") id: Long
    ): OpStatus
}

object TagihanApi {
    val service: TagihanApiService by lazy { retrofit.create(TagihanApiService::class.java) }
    fun getImageUrl(imageId: String): String = "${BASE_URL}images/$imageId.jpg"
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }