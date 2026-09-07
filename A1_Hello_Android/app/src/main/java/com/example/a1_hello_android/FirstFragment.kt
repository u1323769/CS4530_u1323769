package com.example.a1_hello_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


/**
 * First fragment class that holds the buttons
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonRed = view.findViewById<Button>(R.id.buttonRed)
        val buttonOrange = view.findViewById<Button>(R.id.buttonOrange)
        val buttonYellow = view.findViewById<Button>(R.id.buttonYellow)
        val buttonGreen = view.findViewById<Button>(R.id.buttonGreen)
        val buttonBlue = view.findViewById<Button>(R.id.buttonBlue)

        // Once button is clicked, calls helper method that opens the second fragment
        buttonRed.setOnClickListener {
            openSecondFrag(buttonRed.text.toString())
        }

        buttonOrange.setOnClickListener {
            openSecondFrag(buttonOrange.text.toString())
        }

        buttonYellow.setOnClickListener {
            openSecondFrag(buttonYellow.text.toString())
        }

        buttonGreen.setOnClickListener {
            openSecondFrag(buttonGreen.text.toString())
        }

        buttonBlue.setOnClickListener {
            openSecondFrag(buttonBlue.text.toString())
        }
    }

    /**
     * Helper method that opens the second fragment once the button is clicked
     * shows the text of the button clicked on the second fragment
     */
    private fun openSecondFrag(selectedText: String) {
        val secondFrag = SecondFragment()
        val bundle = Bundle()
        bundle.putString("selected_text", selectedText)
        secondFrag.arguments = bundle
        parentFragmentManager.beginTransaction()
            // Replaces the first fragment with the second fragment
            .replace(R.id.fragment_container, secondFrag)
            // Stores previous first fragment
            .addToBackStack(null)
            // Performs transaction
            .commit()
    }
}