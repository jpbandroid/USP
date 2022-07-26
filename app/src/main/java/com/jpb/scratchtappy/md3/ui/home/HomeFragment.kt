package com.jpb.scratchtappy.md3.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jpb.scratchtappy.md3.R
import com.jpb.scratchtappy.md3.databinding.FragmentHomeBinding
import android.widget.Button


class HomeFragment : Fragment() {
    var tap = 0

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val root2 = inflater.inflate(com.jpb.scratchtappy.md3.R.layout.fragment_home, null) as ViewGroup
        val but = root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button) as Button
        val text = root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.text3) as TextView
        but.setOnClickListener {
            tap++
            text.setText(tap.toString())
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}