package com.example.taskmanagerhw4

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerhw4.databinding.TaskListBinding


// Fragment with all tasks listed
class TaskListFragment : Fragment() {

    private var _binding: TaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = TaskListBinding.inflate(inflater, container, false)

        this.context?.let { ctx ->
            val taskListStore = TaskListStore(ctx)

            // View of tasks
            val recyclerView: RecyclerView = binding.taskList
            recyclerView.adapter = TaskAdapter(taskListStore.getAllTasks())

            // Dropdown of sorting choices
            setupSortingDropdown(ctx, taskListStore, recyclerView)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Capture new task click
        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupSortingDropdown(context: Context, taskListStore: TaskListStore, recyclerView: RecyclerView) {
        val sortTypes = SortType.values().map{ type -> type.toString() }

        val dropdown: Spinner = binding.taskFilterSelector
        val dropdownOptionsAdapter: ArrayAdapter<String> =
            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sortTypes)

        dropdown.adapter = dropdownOptionsAdapter
        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                taskListStore.selectSortFilter(SortType.DUE_DATE_ASC.toString())
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                taskListStore.selectSortFilter(sortTypes[position])
                recyclerView.adapter = TaskAdapter(taskListStore.getAllTasks())
            }
        }
    }
}