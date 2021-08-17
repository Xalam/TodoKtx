package com.example.todo.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.data.Category
import com.example.todo.databinding.CategoryAddDialogBinding
import com.example.todo.databinding.FragmentCategoryBinding
import com.example.todo.ui.adapter.CategoryAdapter
import com.example.todo.utils.ItemClick
import com.example.todo.viewmodel.TodoViewModel

class CategoryFragment : Fragment(), View.OnClickListener, ItemClick {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var bindingDialog: CategoryAddDialogBinding
    val categoryAdapter = CategoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddCat.setOnClickListener(this)

        with(binding.rvCategory) {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.getAllCategory.observe(viewLifecycleOwner, Observer { cat ->
            categoryAdapter.setData(cat)
        })

        categoryAdapter.setItemClick(this)
    }

    override fun onClick(v: View?) {
        if (v == binding.buttonAddCat) {
            categoryAddDialog()
        }
    }

    override fun onItemClickStatus(position: Int, status: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteCategory(position)
            todoViewModel.deleteTodoCategory(position)
            Toast.makeText(requireContext(), "Successfully removed", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Category")
        builder.setMessage("Are you sure you want to delete?")
        builder.create().show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun categoryAddDialog() {
        val dialogCategory = Dialog(requireContext())

        bindingDialog = CategoryAddDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialogCategory.setContentView(bindingDialog.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogCategory.window?.setBackgroundDrawable(requireContext().resources.getDrawable(R.drawable.bg_round_dialog, requireContext().theme))
        }

        bindingDialog.buttonCancelCategory.setOnClickListener { dialogCategory.dismiss() }
        bindingDialog.buttonSaveCategory.setOnClickListener {
            insertCategoryDatabase()
            dialogCategory.dismiss()
        }

        dialogCategory.window?.setLayout(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
        dialogCategory.setCanceledOnTouchOutside(false)
        dialogCategory.show()
    }

    private fun insertCategoryDatabase() {
        val categoryInput = bindingDialog.edtNewCategory.text.toString()

        if (inputCheck(categoryInput, "Input")) {
            val category = Category(null, categoryInput)
            todoViewModel.addCategoryTodo(category)
            Toast.makeText(requireContext(), "Record inserted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please input category", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }
}