package com.example.a1_hello_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

/**
 * Second fragment that will show the text of the selected button
 * Also has a back button that will go back to the first fragment when clicked
 */
class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedTextView = view.findViewById<TextView>(R.id.selectedTextView)
        val backButton = view.findViewById<Button>(R.id.backButton)
        // Retrieves stored selected text
        val selectedText = arguments?.getString("selected_text")

        selectedTextView.text = "$selectedText"
        // Goes back to previous fragment using popBackStack
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}