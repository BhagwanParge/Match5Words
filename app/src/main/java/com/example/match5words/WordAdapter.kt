package com.example.match5words

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.match5words.model.Word

class WordAdapter(private val listener: OnActionsListener): ListAdapter<Word, WordAdapter.VH>(DIFF) {

    interface OnActionsListener {
        fun onCopy(text: String)
        fun onShare(text: String)
        fun onFavorite(word: Word)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Word>() {
            override fun areItemsTheSame(oldItem: Word, newItem: Word) =
                oldItem.english == newItem.english

            override fun areContentsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tEnglish: TextView = itemView.findViewById(R.id.tvEnglish)
        private val tHindi: TextView = itemView.findViewById(R.id.tvHindi)
        private val tMarathi: TextView = itemView.findViewById(R.id.tvMarathi)
        private val btnCopy: ImageButton = itemView.findViewById(R.id.btnCopy)
        private val btnShare: ImageButton = itemView.findViewById(R.id.btnShare)
        private val btnFav: ImageButton = itemView.findViewById(R.id.btnFav)

        fun bind(w: Word) {
            tEnglish.text = w.english
            tHindi.text = w.hindi
            tMarathi.text = w.marathi

            val combined = "${w.english} - Hindi: ${w.hindi} | Marathi: ${w.marathi}"

            btnCopy.setOnClickListener { listener.onCopy(combined) }
            btnShare.setOnClickListener { listener.onShare(combined) }
            btnFav.setOnClickListener { listener.onFavorite(w) }
        }
    }
}
