package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufalsulthanfakhry0092.asesmenmobpro1.database.TagihanDao
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecycleBinViewModel(
    private val dao: TagihanDao
) : ViewModel() {

    val data: StateFlow<List<Tagihan>> = dao.getDeletedTagihan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun restore(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.restore(id)
        }
    }

    fun deletePermanent(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deletePermanent(id)
        }
    }
}