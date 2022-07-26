package com.jpb.scratchtappy.md3.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jpb.scratchtappy.md3.R
import com.jpb.scratchtappy.md3.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    var tap = 0

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val but = root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.floatingActionButton2) as FloatingActionButton
        val text = root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.text7) as TextView
        but.setOnClickListener {
            tap++
            text.setText(Integer.toString(tap))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}