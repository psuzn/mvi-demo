package me.sujanpoudel.mvidemo.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import me.sujanpoudel.mvidemo.Config
import me.sujanpoudel.mvidemo.data.RoomDB
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun getRoomDb(@ApplicationContext context: Context): RoomDB {
        return Room.databaseBuilder(context, RoomDB::class.java, Config.DATABASE_NAME).build()
    }
}