package com.example.deaft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class MenuTranslateFragment: Fragment() {

    private lateinit var btnSelectVibration: Button
    private lateinit var btnSelectLibras: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para este fragmento
        return inflater.inflate(R.layout.fragment_menu_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSelectLibras = view.findViewById(R.id.btn_select_dic_com)
        btnSelectVibration = view.findViewById(R.id.btn_select_vibration)

        setupMenuTranslateFragment()
    }

    private fun setupMenuTranslateFragment() {
        btnSelectVibration.setOnClickListener {
            val intent = Intent(requireContext(), MenuTranslateActivity::class.java)
            startActivity(intent)
        }
        btnSelectLibras.setOnClickListener {
            val intent = Intent(requireContext(), DicComActivity::class.java)
            startActivity(intent)
        }
    }
}