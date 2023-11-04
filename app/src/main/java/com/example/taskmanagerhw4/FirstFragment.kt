package com.example.taskmanagerhw4

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
import com.example.taskmanagerhw4.databinding.ActivityMainBinding
import com.example.taskmanagerhw4.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        this.context?.let { ctx ->
            val dropdown: Spinner = binding.taskFilterSelector
            val items = SortType.values().map{ type -> type.toString() }
            val taskListStore = TaskListStore(ctx)

            val a = taskListStore.getAllTasks()
            var customAdapter = CustomAdapter(a)
//            customAdapter.notifyDataSetChanged()

            val recyclerView: RecyclerView = binding.taskList
            recyclerView.adapter = customAdapter


            val adapter: ArrayAdapter<String> =
                ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, items)

            dropdown.adapter = adapter
            dropdown?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    taskListStore.selectSortFilter(SortType.DUE_DATE_ASC.toString())
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    taskListStore.selectSortFilter(items[position])
//                    val a = taskListStore.getAllTasks()
//                    customAdapter = CustomAdapter(a)
                    customAdapter = CustomAdapter(taskListStore.getAllTasks())
                    recyclerView.adapter = customAdapter
                }

            }



        }
        return binding.root

    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}