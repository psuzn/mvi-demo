package me.sujanpoudel.mvidemo.ui.newItem

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import me.sujanpoudel.mvidemo.R
import me.sujanpoudel.mvidemo.base.mvi.MviSheet
import me.sujanpoudel.mvidemo.databinding.NewNoteSheetBinding
import me.sujanpoudel.mvidemo.ui.newItem.NewNoteUIAction.SaveNote

@AndroidEntryPoint
class NewNoteSheet : MviSheet<NewNoteUIAction, NewNoteState>() {
    private lateinit var binding: NewNoteSheetBinding
    override val viewModel by viewModels<NewNoteViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = NewNoteSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog).apply {
            setCanceledOnTouchOutside(true)
        }
    }

    override fun render(state: NewNoteState) {
        binding.tilTitle.error = state.titleError
        state.saved.value?.let {
            toast("Note Saved")
            dismiss()
        }
    }

    override fun actions(): Observable<NewNoteUIAction> {
        return Observable.mergeArray(
            binding.btnAdd.clicks().map { SaveNote(binding.etTitle.text.toString(), binding.etDescription.text.toString()) },
            binding.etTitle.textChanges().map { NewNoteUIAction.TitleChanged }
        )
    }
}