package me.sujanpoudel.mvidemo.ui.notes

import androidx.hilt.lifecycle.ViewModelInject
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import me.sujanpoudel.mvidemo.base.mvi.MviViewModel
import me.sujanpoudel.mvidemo.data.RoomDB
import me.sujanpoudel.mvidemo.data.models.Note
import me.sujanpoudel.mvidemo.ui.notes.NotesListItem.*
import java.util.concurrent.TimeUnit

class NotesViewModel @ViewModelInject constructor(
    private val roomDB: RoomDB
) : MviViewModel<NotesUIAction, NotesState, NotesInternalAction>(NotesState()) {
    override fun transform(action: NotesUIAction): Observable<NotesInternalAction> {
        return when (action) {
            NotesUIAction.InitialAction -> getNotes()
            is NotesUIAction.CompleteChanges -> toggleComplete(action)
            is NotesUIAction.ArchiveChanges -> toggleArchived(action)
        }
    }

    private fun toggleComplete(action: NotesUIAction.CompleteChanges) =
        roomDB.noteDao.update(action.noteItem.note.copy(completed = !action.noteItem.note.completed))
            .subscribeOn(Schedulers.io())
            .andThen(Observable.empty<NotesInternalAction>())

    private fun toggleArchived(action: NotesUIAction.ArchiveChanges) =
        roomDB.noteDao.update(action.noteItem.note.copy(archived = !action.noteItem.note.archived))
            .subscribeOn(Schedulers.io())
            .andThen(Observable.empty<NotesInternalAction>())
            .startWith(NotesInternalAction.TmpRemove(action.noteItem)) //to prevent swiped empty view from moving, remove it first from list

    private fun getNotes() = roomDB.noteDao.getAll()
        .subscribeOn(Schedulers.io())
        .map { NotesInternalAction.NotesResult(it) as NotesInternalAction }

    override fun reduce(state: NotesState, action: NotesInternalAction): NotesState {
        return when (action) {
            is NotesInternalAction.NotesResult -> {
                return if (action.notes.isEmpty()) state.copy(noteListItem = emptyList())
                else state.copy(noteListItem = groupNotes(action.notes))
            }
            is NotesInternalAction.TmpRemove -> state.copy(noteListItem = state.noteListItem.filter { action.noteItem != it })
        }
    }

    private fun groupNotes(notes: List<Note>): List<NotesListItem> {
        val archived = notes.filter { it.archived }.map { NoteItem(it) }
        val completed = notes.filter { it.completed && !it.archived }.map { NoteItem(it) }
        val uncompleted = notes.filter { !it.completed && !it.archived }.map { NoteItem(it) }

        return mutableListOf<NotesListItem>().apply {
            add(HeaderItem("Notes"))
            if (uncompleted.isEmpty()) add(EmptyItem("normal", "Empty"))
            else addAll(uncompleted)
            add(HeaderItem("Completed"))
            if (completed.isEmpty()) add(EmptyItem("completed", "Empty"))
            else addAll(completed)
            add(HeaderItem("Archived"))
            if (archived.isEmpty()) add(EmptyItem("archived", "Empty"))
            else addAll(archived)
        }
    }
}