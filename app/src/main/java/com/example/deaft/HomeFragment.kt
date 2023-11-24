package com.example.deaft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lógica específica do HomeFragment
        setupHomeFragment()
    }

    private fun setupHomeFragment() {
        // Implemente a lógica específica do HomeFragment aqui
        //textViewHome.text = "Bem-vindo à tela Home!"
        // ...
    }
}

