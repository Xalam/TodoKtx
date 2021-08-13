package com.example.todo.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.data.Category
import com.example.todo.data.Todo
import com.example.todo.databinding.BottomAddDialogBinding
import com.example.todo.databinding.CategoryAddDialogBinding
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ui.adapter.TodoAdapter
import com.example.todo.viewmodel.TodoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var bindingBottom: BottomAddDialogBinding
    private lateinit var bindingDialog: CategoryAddDialogBinding
    private var listCategory = ArrayList<Category>()

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
        binding.buttonCategoryAdd.setOnClickListener(this)

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

        //Add Menu
        setHasOptionsMenu(true)
    }

    override fun onClick(v: View?) {
        if (v == binding.floatingAdd) {
            showBottomDialog()
        } else if (v == binding.buttonCategoryAdd) {
            categoryAddDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllDatabase() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteAllTodos()

            Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete All Todos")
        builder.setMessage("Are you sure you want to delete all todos?")
        builder.create().show()
    }

    private fun showBottomDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)

        bindingBottom = BottomAddDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(bindingBottom.root)

        bindingBottom.chipCategory.setOnClickListener { categoryShowMenu() }
        bindingBottom.floatingSave.setOnClickListener {
            insertDatabase()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun insertDatabase() {
        val title = bindingBottom.edtTitleAdd.text.toString()
        val description = bindingBottom.edtDescriptionAdd.text.toString()
        val category = bindingBottom.chipCategoryId.text.toString()
        var catId = 0

        if (!TextUtils.isEmpty(category)) {
            catId = category.toInt()
        }

        if (inputCheck(title, description)) {
            val todo = Todo(null, title, description, catId, "2021-08-10", 0)
            todoViewModel.addTodo(todo)
            Toast.makeText(requireContext(), "Record inserted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

    private fun categoryShowMenu() {
        val popMenu = PopupMenu(requireContext(), bindingBottom.chipCategory)

        todoViewModel.getAllCategory.observe(viewLifecycleOwner, Observer { cat ->
            val l = cat.size - 1
            for (i in 0..l) {
                popMenu.menu.add(i, cat[i].id!!, i, cat[i].cat_title)
            }
        })

        popMenu.setOnMenuItemClickListener { item: MenuItem? ->
            bindingBottom.chipCategory.text = item!!.title
            bindingBottom.chipCategoryId.text = item.itemId.toString()
            true
        }
        popMenu.show()
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
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }
}