package me.sujanpoudel.mvidemo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val description: String,
    val completed: Boolean = false,
    val archived: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)
