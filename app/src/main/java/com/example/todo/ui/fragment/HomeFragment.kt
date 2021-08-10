package com.example.todo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ui.adapter.TodoAdapter
import com.example.todo.viewmodel.TodoViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingAdd.setOnClickListener(this)

        //RecyclerView
        val todoAdapter = TodoAdapter()
        with(binding.rvTodo) {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        //ViewModel
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer { todo ->
            todoAdapter.setData(todo)
        })
    }

    override fun onClick(v: View?) {
        if (v == binding.floatingAdd) {
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
    }
}