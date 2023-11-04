package com.example.taskmanagerhw4

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskmanagerhw4.databinding.FragmentSecondBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var taskListStore: TaskListStore


            override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createTaskDescription.doAfterTextChanged { checkForComplete() }
        binding.createTaskTitle.doAfterTextChanged { checkForComplete() }
        binding.createTaskDueDate.doAfterTextChanged { checkForComplete() }

        taskListStore = TaskListStore(view.context)
        val editedItem = taskListStore?.getCurrentlyEdited()
        if (editedItem != null) {
            binding.createTaskTitle.setText(editedItem.title)
            binding.createTaskDescription.setText(editedItem.description)
            binding.createTaskDueDate.setText(editedItem.dueDate)
            binding.createTaskPriority.setProgress(editedItem.priorityLevel)
//            https://stackoverflow.com/questions/64457240/i-want-to-replace-snackbar-with-alert-dialog-how-in-kotlin

            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setMessage("This is the message")
                    setTitle("This is the title")
                    setPositiveButton("Yes, delete",
                        DialogInterface.OnClickListener { dialog, id ->
                            taskListStore.deleteTask(editedItem.uuid.toString())
                            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                        })
                    setNegativeButton("No, cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                            // Use this to dismiss dialog
                            dialog.dismiss()
                        })
                }
                // Set other dialog properties

                // Create the AlertDialog
                builder.create()
            }
            binding.deleteTaskButton.setOnClickListener { alertDialog?.show() }

            binding.createButton.setOnClickListener {
                editedItem.title = binding.createTaskTitle.text.toString()
                editedItem.description = binding.createTaskDescription.text.toString()
                editedItem.priorityLevel = binding.createTaskPriority.progress
                editedItem.dueDate = binding.createTaskDueDate.text.toString()
                taskListStore.updateTask(editedItem)
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }


        } else {
            binding.deleteTaskButton.isVisible = false
            binding.createButton.setOnClickListener {
                taskListStore?.addTask(TaskItem(
                    title = binding.createTaskTitle.text.toString(),
                    description = binding.createTaskDescription.text.toString(),
                    priorityLevel = binding.createTaskPriority.progress,
                    dueDate = binding.createTaskDueDate.text.toString()
                ))
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }

        binding.createTaskPriority.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // here, you react to the value being set in seekBar
                seekBar.thumbTintList = ColorStateList.valueOf(TaskItem.getPriorityColor(seekBar.progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })

        val textView: TextView = binding.createTaskDueDate
        textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            textView.text = sdf.format(cal.time)

        }

        textView.setOnClickListener {
            this.context?.let { ctx ->
                DatePickerDialog(
                    ctx, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        taskListStore?.stopEditting()
    }

    fun checkForComplete() {
        var dateValid = false
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            sdf.setLenient(false)
            sdf.parse(binding.createTaskDueDate.text.toString())
            dateValid = true
        } catch (e: ParseException) {}

        if (binding.createTaskDescription.text.isNotEmpty()
            && binding.createTaskTitle.text.isNotEmpty()
            && dateValid
        ) {
            binding.createButton.isEnabled = true
            binding.createTaskHint.isVisible = false
        } else {
            binding.createButton.isEnabled = false
            binding.createTaskHint.isVisible = true
        }
    }


}
