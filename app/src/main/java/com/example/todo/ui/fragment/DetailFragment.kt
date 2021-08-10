package com.example.todo.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.data.Todo
import com.example.todo.databinding.FragmentDetailBinding
import com.example.todo.viewmodel.TodoViewModel

class DetailFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSubmit.setOnClickListener(this)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
    }

    override fun onClick(v: View?) {
        if (v == binding.buttonSubmit) {
            insertDatabase()
        }
    }

    private fun insertDatabase() {
        val title = binding.textTitle.text.toString()
        val description = binding.textDescription.text.toString()

        if (inputCheck(title, description)) {
            val todo = Todo(null, title, description, 1, "2021-08-10", 1)
            todoViewModel.addTodo(todo)
            Toast.makeText(requireContext(), "Record inserted", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }
}