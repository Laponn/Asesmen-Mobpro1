package com.naufalsulthanfakhry0092.asesmenmobpro1.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.naufalsulthanfakhry0092.asesmenmobpro1.database.TagihanDb
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen.DetailViewModel
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen.MainViewModel
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen.RecycleBinViewModel

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = TagihanDb.getInstance(context).dao

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }  else if (modelClass.isAssignableFrom(RecycleBinViewModel::class.java)) {
        return RecycleBinViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}