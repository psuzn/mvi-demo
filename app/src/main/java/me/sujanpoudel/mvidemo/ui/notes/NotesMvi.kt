package me.sujanpoudel.mvidemo.ui.notes

import me.sujanpoudel.mvidemo.base.mvi.Action
import me.sujanpoudel.mvidemo.base.mvi.InitAction
import me.sujanpoudel.mvidemo.base.mvi.InternalAction
import me.sujanpoudel.mvidemo.base.mvi.State
import me.sujanpoudel.mvidemo.data.models.Note

data class NotesState(
    val noteListItem: List<NotesListItem> = emptyList()
) : State() {
    val isEmpty = noteListItem.isEmpty()
}

sealed class NotesUIAction : Action() {
    object InitialAction : NotesUIAction(), InitAction
    class CompleteChanges(val noteItem: NotesListItem.NoteItem):NotesUIAction()
    class ArchiveChanges(val noteItem: NotesListItem.NoteItem):NotesUIAction()
}

sealed class NotesInternalAction : InternalAction() {
    class NotesResult(val notes: List<Note>) : NotesInternalAction()
    class TmpRemove(val noteItem: NotesListItem.NoteItem) : NotesInternalAction()
}

sealed class NotesListItem {
    class NoteItem(val note: Note) : NotesListItem()
    class HeaderItem(val title: String) : NotesListItem()
    class EmptyItem(val type:String,val message: String) : NotesListItem()
}