package com.example.taskmanagerhw4

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerhw4.storage.task.TaskItem

// Adapter that builds and creates bindings for tasks shown to user
// Based on https://developer.android.com/develop/ui/views/layout/recyclerview
class TaskAdapter(private val taskList: Array<TaskItem>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val taskListStore = TaskListStore(view.context)

        val titleView: TextView
        val descriptionView: TextView
        val completedStatusBox: CheckBox
        val dueDateText: TextView
        val editButton: ImageButton

        init {
            titleView = view.findViewById(R.id.taskItemTitle)
            descriptionView = view.findViewById(R.id.taskItemDescription)
            completedStatusBox = view.findViewById(R.id.taskItemCompleted)
            dueDateText = view.findViewById(R.id.taskItemDueDate)
            editButton = view.findViewById(R.id.taskItemEditAction)
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Set shown values to actual ones
        (viewHolder.titleView.text) = taskList[position].title
        (viewHolder.descriptionView.text) = taskList[position].description
        (viewHolder.completedStatusBox.isChecked) = taskList[position].completed
        (viewHolder.completedStatusBox.buttonTintList) = ColorStateList.valueOf(taskList[position].getPriorityColor())
        (viewHolder.dueDateText.text) = taskList[position].getFormattedDate()

        // Create edit button and checkbox listener
        (viewHolder.editButton.setOnClickListener { view ->
            viewHolder.taskListStore.setCurrentlyEdited(taskList[position].uuid.toString())
            view.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        })
        (viewHolder.completedStatusBox.setOnCheckedChangeListener{ _, isChecked ->
            viewHolder.taskListStore.markAsCompleted(taskList[position], isChecked)
        })
    }


    override fun getItemCount() = taskList.size
}
