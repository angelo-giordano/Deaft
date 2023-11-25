package com.example.deaft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText


class LibraFragment : Fragment() {

    private lateinit var inputText: TextInputEditText
    private lateinit var searchBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_libra, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBtn = view.findViewById(R.id.searchBtn)
        inputText = view.findViewById(R.id.inputText)
        // Lógica específica do HomeFragment
        setupLibraFragment()
    }

    private fun setupLibraFragment() {
        // Implemente a lógica específica do HomeFragment aqui
        searchBtn.setOnClickListener {
            val text = inputText.text.toString()
            inputText.setText(text + " funfou")
        }

    }
}