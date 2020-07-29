package me.sujanpoudel.mvidemo.ui.newItem

import androidx.hilt.lifecycle.ViewModelInject
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import me.sujanpoudel.mvidemo.base.mvi.MviViewModel
import me.sujanpoudel.mvidemo.base.mvi.SingleEvent
import me.sujanpoudel.mvidemo.data.RoomDB
import me.sujanpoudel.mvidemo.data.models.Note
import me.sujanpoudel.mvidemo.ui.newItem.NewNoteInternalAction.TitleError

class NewNoteViewModel @ViewModelInject constructor(
    private val roomDB: RoomDB
) : MviViewModel<NewNoteUIAction, NewNoteState, NewNoteInternalAction>(NewNoteState()) {

    override fun transform(action: NewNoteUIAction): Observable<NewNoteInternalAction> {
        return when (action) {
            is NewNoteUIAction.SaveNote -> save(action)
            NewNoteUIAction.TitleChanged -> Observable.just(TitleError())
        }
    }

    private fun save(action: NewNoteUIAction.SaveNote): Observable<NewNoteInternalAction> {
        return if (action.title.trim().isEmpty())
            Observable.just(TitleError("Please enter title"))
        else
            roomDB.noteDao.insert(Note(action.title, action.description))
                .subscribeOn(Schedulers.io())
                .andThen(Observable.just(NewNoteInternalAction.NoteSaved as NewNoteInternalAction))
    }

    override fun reduce(state: NewNoteState, action: NewNoteInternalAction): NewNoteState {
        return when (action) {
            is TitleError -> state.copy(titleError = action.error)
            NewNoteInternalAction.NoteSaved -> state.copy(titleError = null, saved = SingleEvent(Unit))
        }
    }
}