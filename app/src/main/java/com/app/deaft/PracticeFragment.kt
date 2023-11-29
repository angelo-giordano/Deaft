package com.app.deaft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class PracticeFragment : Fragment() {

    private lateinit var btnSelectLearn: Button
    private lateinit var btnSelectRecognize: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para este fragmento
        return inflater.inflate(R.layout.fragment_practice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSelectLearn = view.findViewById(R.id.btn_select_learn)
        btnSelectRecognize = view.findViewById(R.id.btn_select_recognize)

        setupPracticeFragment()
    }

    private fun setupPracticeFragment() {
        btnSelectLearn.setOnClickListener {
            val intent = Intent(context, LearnActivity::class.java)
            startActivity(intent)
        }
        btnSelectRecognize.setOnClickListener {
            val intent = Intent(context, RecognizeActivity::class.java)
            startActivity(intent)
        }
    }

}
