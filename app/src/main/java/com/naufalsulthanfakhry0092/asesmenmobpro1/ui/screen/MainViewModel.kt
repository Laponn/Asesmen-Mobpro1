package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufalsulthanfakhry0092.asesmenmobpro1.database.TagihanDao
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan
import com.naufalsulthanfakhry0092.asesmenmobpro1.network.ApiStatus
import com.naufalsulthanfakhry0092.asesmenmobpro1.network.TagihanApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(private val dao: TagihanDao) : ViewModel() {

    private val _data = MutableStateFlow<List<Tagihan>>(emptyList())
    val data: StateFlow<List<Tagihan>> get() = _data

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun clearMessage() { errorMessage.value = null }

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId.isEmpty()) {
                _data.value = emptyList()
                status.value = ApiStatus.SUCCESS
                return@launch
            }

            status.value = ApiStatus.LOADING
            try {
                _data.value = TagihanApi.service.getTagihan(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(
        userId: String,
        nama: String,
        total: Int,
        jumlahOrang: Int,
        pakaiPajak: Boolean,
        persentasePajak: Double,
        hasilPerOrang: Double,
        bitmap: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pajakStatus = if (pakaiPajak) "1" else "0"
                val tanggalDibuat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                val result = TagihanApi.service.postTagihan(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    total.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    jumlahOrang.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    pajakStatus.toRequestBody("text/plain".toMediaTypeOrNull()),
                    persentasePajak.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    hasilPerOrang.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    tanggalDibuat.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success" || result.status == null) {
                    retrieveData(userId)
                } else {
                    throw Exception(result.message ?: "Gagal")
                }
            } catch (e: Exception) {
                var errorMsg = e.message ?: "Unknown Error"
                if (e is HttpException) {
                    errorMsg = e.response()?.errorBody()?.string() ?: errorMsg
                }
                errorMessage.value = errorMsg
            }
        }
    }

    fun updateData(
        userId: String,
        id: Long,
        nama: String,
        total: Int,
        jumlahOrang: Int,
        pakaiPajak: Boolean,
        persentasePajak: Double,
        hasilPerOrang: Double,
        bitmap: Bitmap?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val pajakStatus = if (pakaiPajak) "1" else "0"
                val method = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
                val imgPart = bitmap?.toMultipartBody()

                val result = TagihanApi.service.updateTagihan(
                    userId,
                    id,
                    method,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    total.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    jumlahOrang.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    pajakStatus.toRequestBody("text/plain".toMediaTypeOrNull()),
                    persentasePajak.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    hasilPerOrang.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    imgPart
                )

                if (result.status == "success" || result.status == null) {
                    retrieveData(userId)
                } else {
                    throw Exception(result.message ?: "Gagal update data")
                }
            } catch (e: Exception) {
                var errorMsg = e.message ?: "Unknown Error"
                if (e is HttpException) {
                    errorMsg = e.response()?.errorBody()?.string() ?: errorMsg
                }
                errorMessage.value = errorMsg
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun moveToRecycleBin(userId: String, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val result = TagihanApi.service.softDeleteTagihan(userId, id)
                if (result.status == "success" || result.status == null) {
                    retrieveData(userId)
                } else {
                    throw Exception(result.message ?: "Gagal menghapus data")
                }
            } catch (e: Exception) {
                var errorMsg = e.message ?: "Unknown Error"
                if (e is HttpException) {
                    errorMsg = e.response()?.errorBody()?.string() ?: errorMsg
                }
                errorMessage.value = errorMsg
                status.value = ApiStatus.FAILED
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }
}