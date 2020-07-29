package me.sujanpoudel.mvidemo.data.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import me.sujanpoudel.mvidemo.data.models.Note

@Dao
abstract class NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(note: Note): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(notes: List<Note>): Completable

    @Query("SELECT * FROM Note ORDER BY ID DESC")
    abstract fun getAll(): Observable<List<Note>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(note: Note) :Completable
}
