package com.naufalsulthanfakhry0092.asesmenmobpro1.model

data class Tagihan(
    val id: Long = 0L,
    val namaTagihan: String,
    val totalTagihan: Double,
    val jumlahOrang: Int,
    val pakaiPajak: Boolean,
    val persentasePajak: Double,
    val hasilPerOrang: Double,
    val tanggalDibuat: String
)
