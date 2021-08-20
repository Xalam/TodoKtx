package com.example.todo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.FragmentCompletedBinding
import com.example.todo.ui.adapter.TodoAdapter
import com.example.todo.utils.ItemClick
import com.example.todo.viewmodel.TodoViewModel

class CompletedFragment : Fragment(), ItemClick {

    private lateinit var binding: FragmentCompletedBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerView
        val todoAdapter = TodoAdapter()

        with(binding.rvTodoCompleted) {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        //ViewModel
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.getAllData(1).observe(viewLifecycleOwner, Observer { todo ->
            todoAdapter.setData(todo)
            if (todo.isEmpty()) {
                binding.relTextNothing.visibility = View.VISIBLE
            } else {
                binding.relTextNothing.visibility = View.GONE
            }
        })
        todoAdapter.setItemClick(this)
    }

    override fun onItemClickStatus(position: Int, status: Int) {
        val statusNum = if (status == 0) 1 else 0
        todoViewModel.updateStatusTodo(statusNum, position)
    }
}