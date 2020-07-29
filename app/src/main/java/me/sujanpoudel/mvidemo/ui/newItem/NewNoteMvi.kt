package me.sujanpoudel.mvidemo.ui.newItem

import me.sujanpoudel.mvidemo.base.mvi.Action
import me.sujanpoudel.mvidemo.base.mvi.InternalAction
import me.sujanpoudel.mvidemo.base.mvi.SingleEvent
import me.sujanpoudel.mvidemo.base.mvi.State

data class NewNoteState(
    val titleError: String? = null,
    val saved: SingleEvent<Unit> = SingleEvent()
) : State()

sealed class NewNoteUIAction : Action() {
    class SaveNote(val title: String, val description: String) : NewNoteUIAction()
    object TitleChanged : NewNoteUIAction()
}

sealed class NewNoteInternalAction : InternalAction() {
    class TitleError(val error: String? = null) : NewNoteInternalAction()
    object NoteSaved : NewNoteInternalAction()
}

