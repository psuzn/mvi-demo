package me.sujanpoudel.mvidemo.ui.notes

import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import me.sujanpoudel.mvidemo.databinding.ItemEmptyBinding
import me.sujanpoudel.mvidemo.databinding.ItemHeaderBinding
import me.sujanpoudel.mvidemo.databinding.ItemNoteBinding
import me.sujanpoudel.mvidemo.helpers.layoutInflater
import javax.inject.Inject

@FragmentScoped
class NoteItemsAdaptor @Inject constructor() : androidx.recyclerview.widget.ListAdapter<NotesListItem, RecyclerView.ViewHolder>(NotesDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NOTE -> NoteHolder(ItemNoteBinding.inflate(parent.layoutInflater, parent, false))
            HEADER -> HeaderHolder(ItemHeaderBinding.inflate(parent.layoutInflater, parent, false))
            else -> EmptyHolder(ItemEmptyBinding.inflate(parent.layoutInflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it) {
                is NotesListItem.NoteItem -> (holder as NoteHolder?)?.bind(it)
                is NotesListItem.HeaderItem -> (holder as HeaderHolder?)?.bind(it)
                is NotesListItem.EmptyItem -> (holder as EmptyHolder?)?.bind(it)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NotesListItem.NoteItem -> NOTE
            is NotesListItem.HeaderItem -> HEADER
            is NotesListItem.EmptyItem -> EMPTY
        }
    }

    companion object {
        const val NOTE = 0
        const val HEADER = 1
        const val EMPTY = 2
    }

}

class NoteHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(noteItem: NotesListItem.NoteItem) {
        binding.tvNoteTitle.text = if (noteItem.note.archived) HtmlCompat.fromHtml("<strike>${noteItem.note.title}</strike>", 0)
        else HtmlCompat.fromHtml(noteItem.note.title, 0)
        binding.cbDone.isChecked = noteItem.note.completed
    }
}

class HeaderHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(headerItem: NotesListItem.HeaderItem) {
        binding.tvTitle.text = headerItem.title
    }
}

class EmptyHolder(private val binding: ItemEmptyBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(emptyItem: NotesListItem.EmptyItem) {
        binding.tvTitle.text = emptyItem.message
    }
}

class NotesDiff : DiffUtil.ItemCallback<NotesListItem>() {
    override fun areItemsTheSame(oldItem: NotesListItem, newItem: NotesListItem): Boolean {
        return if (oldItem is NotesListItem.NoteItem && newItem is NotesListItem.NoteItem) oldItem.note.id == newItem.note.id
        else if (oldItem is NotesListItem.HeaderItem && newItem is NotesListItem.HeaderItem) oldItem.title == newItem.title
        else if (oldItem is NotesListItem.EmptyItem && newItem is NotesListItem.EmptyItem) oldItem.type == newItem.type
        else false
    }

    override fun areContentsTheSame(oldItem: NotesListItem, newItem: NotesListItem): Boolean {
        return oldItem == newItem
    }

}