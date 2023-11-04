package com.example.taskmanagerhw4

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskmanagerhw4.databinding.TaskEditBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


// Fragment with an option to create/edit a task
class TaskEditFragment : Fragment() {

    private var _binding: TaskEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskListStore: TaskListStore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = TaskEditBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListStore = TaskListStore(view.context)

        val editedItem = taskListStore.getCurrentlyEdited()
        if (editedItem != null) {
            /**
             * Pre-fill inputs with current data of the task
             */
            binding.createTaskTitle.setText(editedItem.title)
            binding.createTaskDescription.setText(editedItem.description)
            setupCalendarTextInput(editedItem.dueDate)
            binding.createTaskPriority.progress = editedItem.priorityLevel
            binding.createTaskPriority.thumbTintList = ColorStateList.valueOf(TaskItem.getPriorityColor(editedItem.priorityLevel))

            // Capture delete task click
            binding.deleteTaskButton.setOnClickListener { showDeleteTaskDialog(editedItem) }

            // Capture Save edited task click
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
            setupCalendarTextInput(SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis()))
            // Capture Create task click
            binding.createButton.setOnClickListener {
                taskListStore.addTask(TaskItem(
                    title = binding.createTaskTitle.text.toString(),
                    description = binding.createTaskDescription.text.toString(),
                    priorityLevel = binding.createTaskPriority.progress,
                    dueDate = binding.createTaskDueDate.text.toString()
                ))
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }

        binding.createTaskDescription.doAfterTextChanged { checkForComplete() }
        binding.createTaskTitle.doAfterTextChanged { checkForComplete() }
        binding.createTaskDueDate.doAfterTextChanged { checkForComplete() }

        setupSeekBarColorChange()
        checkForComplete()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        taskListStore.stopEditing()
    }


    // Checks if the task creation form is valid.
    // Enables the button if it is complete (date is valid, title and description written)
    private fun checkForComplete() {
        var dateValid = false
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            sdf.isLenient = false
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


    // Displays a dialog to user to confirm for deletion of the task
    // Source: https://stackoverflow.com/questions/64457240/i-want-to-replace-snackbar-with-alert-dialog-how-in-kotlin
    private fun showDeleteTaskDialog(taskItem: TaskItem) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Are you sure you want to delete the task?")
                setTitle("Delete Task")
                setPositiveButton("Yes, delete",
                    DialogInterface.OnClickListener { dialog, id ->
                        taskListStore.deleteTask(taskItem.uuid.toString())
                        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    })
                setNegativeButton("No, cancel",
                    DialogInterface.OnClickListener { dialog, id -> dialog.dismiss() })
            }
            builder.create()
        }

        alertDialog?.show()
    }


    // Seek bar changes its color depending on priority chosen
    // Based on https://stackoverflow.com/questions/71664074/how-to-setonseekbarchangelistener-in-a-kotlin-fragment
    private fun setupSeekBarColorChange() {
        binding.createTaskPriority.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // here, you react to the value being set in seekBar
                seekBar.thumbTintList = ColorStateList.valueOf(TaskItem.getPriorityColor(seekBar.progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }


    // Based on https://stackoverflow.com/questions/45842167/how-to-use-datepickerdialog-in-kotlin
    private fun setupCalendarTextInput(dateString: String) {
        val textView: TextView = binding.createTaskDueDate
        textView.text = dateString

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

}
