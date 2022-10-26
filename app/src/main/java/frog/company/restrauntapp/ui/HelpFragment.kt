package frog.company.restrauntapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import frog.company.restrauntapp.R
import frog.company.restrauntapp.databinding.FragmentHelpBinding
import frog.company.restrauntapp.databinding.FragmentProfileBinding

class HelpFragment : Fragment() {

    private lateinit var _binding : FragmentHelpBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHelpBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return binding.root
    }
}