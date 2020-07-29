package me.sujanpoudel.mvidemo.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.sujanpoudel.mvidemo.base.mvi.MviFragment
import me.sujanpoudel.mvidemo.databinding.FragmentNotesBinding
import me.sujanpoudel.mvidemo.helpers.visible
import me.sujanpoudel.mvidemo.ui.newItem.NewNoteSheet
import me.sujanpoudel.mvidemo.ui.notes.NotesUIAction.ArchiveChanges
import me.sujanpoudel.mvidemo.ui.notes.NotesUIAction.InitialAction
import javax.inject.Inject


@AndroidEntryPoint
class NotesFragment : MviFragment<NotesUIAction, NotesState>() {

    override val viewModel by viewModels<NotesViewModel>()
    private val actionRelay = PublishSubject.create<NotesUIAction>() // it just redirects the actions

    @Inject
    lateinit var adaptor: NoteItemsAdaptor

    var binding: FragmentNotesBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, 0) {
                override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                    return adaptor.getSwipeDir(viewHolder.adapterPosition)
                }

                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    (adaptor.currentList[viewHolder.adapterPosition] as? NotesListItem.NoteItem)?.let {
                        actionRelay.onNext(ArchiveChanges(it))
                    }
                }
            }

        binding?.apply {
            incAppbar.title.text = "Notes"
            rvNotes.adapter = adaptor
            ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(rvNotes)
            fbPlus.setOnClickListener {
                NewNoteSheet().show(childFragmentManager, "new-note-sheet")
            }
        }
    }

    override fun actions(): Observable<NotesUIAction> {
        return Observable.mergeArray(
            adaptor.actions,
            actionRelay,
            Observable.just(InitialAction)
        )
    }

    override fun render(state: NotesState) {
        binding?.apply {
            flEmpty.visible = state.isEmpty
            adaptor.submitList(state.noteListItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
