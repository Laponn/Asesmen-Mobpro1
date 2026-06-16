package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import android.util.Log
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

class MainViewModel(dao: TagihanDao) : ViewModel() {

    private val _data = MutableStateFlow<List<Tagihan>>(emptyList())
    val data: StateFlow<List<Tagihan>> get() = _data
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val result = TagihanApi.service.getTagihan()
                Log.d("MainViewModel", "Success: $result")
                _data.value = result
                status.value = ApiStatus.SUCCESS

            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}