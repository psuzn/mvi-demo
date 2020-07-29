package me.sujanpoudel.mvidemo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.sujanpoudel.mvidemo.Config
import me.sujanpoudel.mvidemo.data.dao.NoteDao
import me.sujanpoudel.mvidemo.data.models.Note
import me.sujanpoudel.mvidemo.data.typeConverter.OffsetDateTimeConverter


@Database(
    entities = [
        Note::class
    ],
    version = Config.DATABASE_VERSION,
    exportSchema = true
)

@TypeConverters(
    OffsetDateTimeConverter::class
)
abstract class RoomDB : RoomDatabase() {
    abstract val noteDao: NoteDao
}