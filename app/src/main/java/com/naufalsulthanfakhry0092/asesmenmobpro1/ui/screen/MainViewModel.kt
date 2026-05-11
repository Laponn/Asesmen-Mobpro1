package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import androidx.lifecycle.ViewModel
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan

class MainViewModel : ViewModel() {

    val data = listOf(
        Tagihan(
            id = 1,
            namaTagihan = "Makan Bakso",
            totalTagihan = 120000.0,
            jumlahOrang = 4,
            pakaiPajak = false,
            persentasePajak = 0.0,
            hasilPerOrang = 30000.0,
            tanggalDibuat = "2026-02-17 12:34:56"
        ),
        Tagihan(
            id = 2,
            namaTagihan = "Nongkrong Kopi",
            totalTagihan = 185000.0,
            jumlahOrang = 5,
            pakaiPajak = true,
            persentasePajak = 10.0,
            hasilPerOrang = 40700.0,
            tanggalDibuat = "2026-02-19 19:20:10"
        ),
        Tagihan(
            id = 3,
            namaTagihan = "Makan Ayam Geprek",
            totalTagihan = 96000.0,
            jumlahOrang = 3,
            pakaiPajak = false,
            persentasePajak = 0.0,
            hasilPerOrang = 32000.0,
            tanggalDibuat = "2026-02-21 13:15:30"
        ),
        Tagihan(
            id = 4,
            namaTagihan = "Pesan Pizza",
            totalTagihan = 250000.0,
            jumlahOrang = 5,
            pakaiPajak = true,
            persentasePajak = 11.0,
            hasilPerOrang = 55500.0,
            tanggalDibuat = "2026-02-25 20:05:44"
        ),
        Tagihan(
            id = 5,
            namaTagihan = "Beli Cemilan Bareng",
            totalTagihan = 78000.0,
            jumlahOrang = 6,
            pakaiPajak = false,
            persentasePajak = 0.0,
            hasilPerOrang = 13000.0,
            tanggalDibuat = "2026-03-01 16:45:22"
        ),
        Tagihan(
            id = 6,
            namaTagihan = "Makan Ramen",
            totalTagihan = 210000.0,
            jumlahOrang = 4,
            pakaiPajak = true,
            persentasePajak = 10.0,
            hasilPerOrang = 57750.0,
            tanggalDibuat = "2026-03-05 18:30:12"
        ),
        Tagihan(
            id = 7,
            namaTagihan = "Patungan Kado Teman",
            totalTagihan = 350000.0,
            jumlahOrang = 7,
            pakaiPajak = false,
            persentasePajak = 0.0,
            hasilPerOrang = 50000.0,
            tanggalDibuat = "2026-03-08 10:12:00"
        )
    )
}