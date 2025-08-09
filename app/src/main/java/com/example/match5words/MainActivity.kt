package com.example.match5words

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.match5words.model.Word
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import kotlin.random.Random

class MainActivity : AppCompatActivity(), WordAdapter.OnActionsListener {

    private lateinit var allWords: List<Word>
    private lateinit var adapter: WordAdapter
    private lateinit var rv: RecyclerView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.recyclerView)
        btnNext = findViewById(R.id.btnNext)

        // load dictionary from assets (default: auto_500.json)
        allWords = loadDictionary("auto_500.json")

        adapter = WordAdapter(this)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        btnNext.setOnClickListener { showNextFive() }

        // first load
        showNextFive()
    }

    private fun loadDictionary(filename: String): List<Word> {
        val input = assets.open(filename)
        val reader = BufferedReader(input.reader())
        val json = reader.use { it.readText() }
        val type = object : TypeToken<List<Word>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun showNextFive() {
        if (allWords.isEmpty()) {
            adapter.submitList(emptyList())
            return
        }
        // randomly pick 5 unique words
        val indices = mutableSetOf<Int>()
        val n = allWords.size
        while (indices.size < 5 && indices.size < n) {
            indices.add(Random.nextInt(n))
        }
        val list = indices.map { allWords[it] }
        adapter.submitList(list)

        // save last set in prefs
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("last_set", Gson().toJson(list)).apply()
    }

    // WordAdapter.OnActionsListener implementations
    override fun onCopy(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    override fun onShare(text: String) {
        val send = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(send, null))
    }

    override fun onFavorite(word: Word) {
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val favJson = prefs.getString("favorites", "[]") ?: "[]"
        val type = object : TypeToken<MutableList<Word>>() {}.type
        val favs: MutableList<Word> = Gson().fromJson(favJson, type)
        if (favs.any { it.english == word.english }) {
            // already present -> remove
            favs.removeAll { it.english == word.english }
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            favs.add(word)
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
        }
        prefs.edit().putString("favorites", Gson().toJson(favs)).apply()
    }
}
