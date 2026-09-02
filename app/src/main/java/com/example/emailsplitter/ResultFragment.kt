package com.example.emailsplitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.emailsplitter.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Read the arguments Bundle set by InputFragment (slide 19)
        val username = arguments?.getString("USERNAME_KEY") ?: ""
        val domain = arguments?.getString("DOMAIN_KEY") ?: ""

        binding.userView.text = username
        binding.domainView.text = domain
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}