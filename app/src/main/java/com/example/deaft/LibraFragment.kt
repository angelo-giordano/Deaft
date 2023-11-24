package com.example.deaft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class LibraFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_libra, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lógica específica do HomeFragment
        setupLibraFragment()
    }

    private fun setupLibraFragment() {
        // Implemente a lógica específica do HomeFragment aqui
        //textViewHome.text = "Bem-vindo à tela Home!"
        // ...
    }
}