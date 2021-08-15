package com.example.todo.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.data.Todo
import com.example.todo.databinding.FragmentEditBinding
import com.example.todo.viewmodel.TodoViewModel

class EditFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentEditBinding
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textTitleEdit.setText(args.currentTodo.title)
        binding.textDescriptionEdit.setText(args.currentTodo.description)

        binding.buttonSubmitEdit.setOnClickListener(this)

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        //Add Menu
        setHasOptionsMenu(true)

    }

    override fun onClick(v: View?) {
        if (v == binding.buttonSubmitEdit) {
            updateDatabase()
        }
    }

    private fun updateDatabase() {
        val title = binding.textTitleEdit.text.toString()
        val description = binding.textDescriptionEdit.text.toString()

        if (inputCheck(title, description)) {
            //Create User Object
            val updateTodo = Todo(args.currentTodo.id, title, description, 1, "2021-08-11", 0)

            //Create Current User
            todoViewModel.updateTodo(updateTodo)
            Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editFragment_to_homeFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun inputCheck(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDatabase() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteTodo(args.currentTodo)

            Toast.makeText(requireContext(), "Successfully removed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editFragment_to_homeFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentTodo.title}")
        builder.setMessage("Are you sure you want to delete ${args.currentTodo.title} ?")
        builder.create().show()
    }
}