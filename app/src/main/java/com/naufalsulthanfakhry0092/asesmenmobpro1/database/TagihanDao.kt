package com.naufalsulthanfakhry0092.asesmenmobpro1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan
import kotlinx.coroutines.flow.Flow

@Dao
interface TagihanDao {

    @Insert
    suspend fun insert(tagihan: Tagihan)

    @Update
    suspend fun update(tagihan: Tagihan)

    @Query("SELECT * FROM tagihan ORDER BY tanggalDibuat DESC")
    fun getTagihan(): Flow<List<Tagihan>>

    @Query("SELECT * FROM tagihan WHERE id = :id")
    suspend fun getTagihanById(id: Long): Tagihan?
}