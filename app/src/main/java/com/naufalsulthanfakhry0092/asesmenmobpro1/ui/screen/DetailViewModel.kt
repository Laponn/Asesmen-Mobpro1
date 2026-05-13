package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufalsulthanfakhry0092.asesmenmobpro1.database.TagihanDao
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(
    private val dao: TagihanDao
) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    suspend fun getTagihan(id: Long): Tagihan? {
        return dao.getTagihanById(id)
    }

    fun insert(
        namaTagihan: String,
        totalTagihan: Double,
        jumlahOrang: Int,
        pakaiPajak: Boolean,
        persentasePajak: Double,
        hasilPerOrang: Double
    ) {
        val tagihan = Tagihan(
            namaTagihan = namaTagihan,
            totalTagihan = totalTagihan,
            jumlahOrang = jumlahOrang,
            pakaiPajak = pakaiPajak,
            persentasePajak = persentasePajak,
            hasilPerOrang = hasilPerOrang,
            tanggalDibuat = formatter.format(Date())
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(tagihan)
        }
    }

    fun update(
        id: Long,
        namaTagihan: String,
        totalTagihan: Double,
        jumlahOrang: Int,
        pakaiPajak: Boolean,
        persentasePajak: Double,
        hasilPerOrang: Double
    ) {
        val tagihan = Tagihan(
            id = id,
            namaTagihan = namaTagihan,
            totalTagihan = totalTagihan,
            jumlahOrang = jumlahOrang,
            pakaiPajak = pakaiPajak,
            persentasePajak = persentasePajak,
            hasilPerOrang = hasilPerOrang,
            tanggalDibuat = formatter.format(Date())
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(tagihan)
        }
    }

    fun delete(id: Long) {
        val deletedAt = formatter.format(Date())

        viewModelScope.launch(Dispatchers.IO) {
            dao.softDelete(id, deletedAt)
        }
    }

    fun restore(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.restore(id)
        }
    }
}