package com.example.emailsplitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.emailsplitter.databinding.FragmentInputBinding

class InputFragment : Fragment() {
    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val pieces = email.split('@')

            if (pieces.size != 2 || pieces.any(String::isEmpty)) {
                Toast.makeText(requireContext(), "Invalid email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val username = pieces[0]
            val domain = pieces[1]

            val resultFragment = ResultFragment()
            val sentData = Bundle()
            sentData.putString("USERNAME_KEY", username)
            sentData.putString("DOMAIN_KEY", domain)
            resultFragment.arguments = sentData

            val fTrans = parentFragmentManager.beginTransaction()
            fTrans.replace(R.id.fl_frag_container, resultFragment, "result_frag")
            fTrans.commit()

            Toast.makeText(requireContext(), "Data passed!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}