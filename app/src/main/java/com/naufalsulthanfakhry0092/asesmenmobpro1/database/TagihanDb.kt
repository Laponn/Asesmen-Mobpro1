package com.naufalsulthanfakhry0092.asesmenmobpro1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan

@Database(
    entities = [Tagihan::class],
    version = 2,
    exportSchema = false
)
abstract class TagihanDb : RoomDatabase() {

    abstract val dao: TagihanDao

    companion object {

        @Volatile
        private var INSTANCE: TagihanDb? = null

        fun getInstance(context: Context): TagihanDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TagihanDb::class.java,
                        "tagihan.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}