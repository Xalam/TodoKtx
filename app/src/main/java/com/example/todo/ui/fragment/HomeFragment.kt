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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.data.Category
import com.example.todo.data.Todo
import com.example.todo.databinding.BottomAddDialogBinding
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ui.adapter.TodoAdapter
import com.example.todo.utils.ItemClick
import com.example.todo.utils.OtherFunction
import com.example.todo.utils.SharedPref
import com.example.todo.viewmodel.TodoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), View.OnClickListener, ItemClick {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var bindingBottom: BottomAddDialogBinding
    private lateinit var sharedPref: SharedPref
    private var listCategory = ArrayList<Category>()
    private val today = MaterialDatePicker.todayInUtcMilliseconds()
    private val calendarTime = Calendar.getInstance()
    private var hourSet = calendarTime.get(Calendar.HOUR_OF_DAY)
    private var minuteSet = calendarTime.get(Calendar.MINUTE)
    val todoAdapter = TodoAdapter()

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
        binding.textCheckAll.setOnClickListener(this)

        //RecyclerView
        with(binding.rvTodo) {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        //ViewModel
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        todoAdapter.setItemClick(this)

        //Add Menu
        setHasOptionsMenu(true)

        //Shared Preference
        sharedPref = SharedPref(requireContext())

        //Chip Category
        binding.chipGroup.removeAllViews()
        chipOptionShow()
    }

    override fun onClick(v: View?) {
        if (v == binding.floatingAdd) {
            showBottomDialog()
        } else if (v == binding.buttonCategoryAdd) {
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        } else if (v == binding.textCheckAll) {
            findNavController().navigate(R.id.action_homeFragment_to_completedFragment)
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
            sharedPref.clearAll()
        }
        bindingBottom.buttonDateAdd.setOnClickListener { openDatePicker() }
        bindingBottom.buttonTimeAdd.setOnClickListener { openTimePicker() }

        val dateToday = OtherFunction.getTodayDate()
        bindingBottom.textDateAdd.text = dateToday.toString()

        dialog.setOnDismissListener {
            sharedPref.clearAll()
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
            var dateSave = dateFormat(today)

            if (sharedPref.getDate().isNotEmpty()) {
                dateSave = sharedPref.getDate()
            }

            if (sharedPref.getTime().isNotEmpty()) {
                val timeString = sharedPref.getTime()
                val timeArray  = arrayOf(timeString.split(":"))
                hourSet = timeArray[0][0].toInt()
                minuteSet = timeArray[0][1].toInt()
            }

            val todo = Todo(null, title, description, catId, "$dateSave $hourSet:$minuteSet", 0)
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
            for (i in cat.indices) {
                if (cat[i].id == 0) {
                    popMenu.menu.add(i, cat[i].id!!, i, R.string.no_category)
                } else {
                    popMenu.menu.add(i, cat[i].id!!, i, cat[i].cat_title)
                }
            }
//            popMenu.menu.add(cat.size, cat.size, cat.size, "Add Category")
        })

        popMenu.setOnMenuItemClickListener { item: MenuItem? ->
            bindingBottom.chipCategory.text = item!!.title
            bindingBottom.chipCategoryId.text = item.itemId.toString()
            true
        }
        popMenu.show()
    }

    private fun openTimePicker() {
        if (sharedPref.getTime().isNotEmpty()) {
            val timeString = sharedPref.getTime()
            val timeArray  = arrayOf(timeString.split(":"))
            hourSet = timeArray[0][0].toInt()
            minuteSet = timeArray[0][1].toInt()
        }

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hourSet)
            .setMinute(minuteSet)
            .setTitleText("Set Time")
            .build()
        picker.show(childFragmentManager, "TAG")

        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute

            sharedPref.setTime("$hour:$minute")

            calendarTime.set(Calendar.HOUR_OF_DAY, hour)
            calendarTime.set(Calendar.MINUTE, minute)
            calendarTime.set(Calendar.SECOND, 0)
            calendarTime.set(Calendar.MILLISECOND, 0)

            picker.dismiss()
        }
    }

    private fun openDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()

        if (sharedPref.getDate().isNotEmpty()) {
            val dateString = sharedPref.getDate()
            val dateArray  = arrayOf(dateString.split("-"))
            val dateMillis = dateInMillis(dateArray[0][2].toInt(), dateArray[0][1].toInt(), dateArray[0][0].toInt())

            builder.setSelection(dateMillis)
            bindingBottom.textDateAdd.text = dateArray[0][2]
        } else {
            builder.setSelection(today)
        }

        val picker = builder.build()

        picker.show(childFragmentManager, picker.toString())

        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)

            val date = calendar.get(Calendar.DAY_OF_MONTH)

            val dateFormat = dateFormat(it)

            sharedPref.setDate(dateFormat)

            bindingBottom.textDateAdd.text = date.toString()
        }
    }

    private fun dateInMillis(day: Int, month: Int, year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar.timeInMillis
    }

    private fun dateFormat(dateLong: Long): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = Date(dateLong)

        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        var monthString = month.toString()
        val year = calendar.get(Calendar.YEAR)

        if (monthString.length == 1) {
            monthString = "0$monthString"
        }

        return "$year-$monthString-$date"
    }

    override fun onItemClickStatus(position: Int, status: Int) {
        val statusNum = if (status == 0) 1 else 0
        todoViewModel.updateStatusTodo(statusNum, position)
    }

    private fun chipOptionShow() {
        todoViewModel.getAllCategory.observe(viewLifecycleOwner, Observer { cat ->
            for (i in cat.indices) {
                val chip = Chip(requireContext())
                val chipDrawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Choice)

                chip.setChipDrawable(chipDrawable)
                chip.text = cat[i].cat_title

                binding.chipGroup.addView(chip)

                val chipGet = binding.chipGroup.getChildAt(i)

                if (i == 0) {
                    chip.isChecked = true
                    todoViewModel.getAllData(0).observe(viewLifecycleOwner, Observer { todo ->
                        todoAdapter.setData(todo)
                        if (todo.isEmpty()) {
                            binding.relTextNothingHome.visibility = View.VISIBLE
                        } else {
                            binding.relTextNothingHome.visibility = View.GONE
                        }
                    })
                }

                chipGet.setOnClickListener {
                    if (cat[i].id == 0) {
                        todoViewModel.getAllData(0).observe(viewLifecycleOwner, Observer { todo ->
                            todoAdapter.setData(todo)
                            if (todo.isEmpty()) {
                                binding.relTextNothingHome.visibility = View.VISIBLE
                            } else {
                                binding.relTextNothingHome.visibility = View.GONE
                            }
                        })
                    } else {
                        todoViewModel.getTodoByCat(cat[i].id!!).observe(viewLifecycleOwner, Observer { t ->
                            todoAdapter.setData(t)
                            if (t.isEmpty()) {
                                binding.relTextNothingHome.visibility = View.VISIBLE
                            } else {
                                binding.relTextNothingHome.visibility = View.GONE
                            }
                        })
                    }
                }

            }
        })
    }
}