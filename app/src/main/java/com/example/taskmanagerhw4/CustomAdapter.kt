package com.example.taskmanagerhw4

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

//Based on  https://developer.android.com/develop/ui/views/layout/recyclerview
class CustomAdapter(private val dataSet: Array<TaskItem>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskListStore = TaskListStore(view.context)

        val titleView: TextView
        val descriptionView: TextView
        val completedStatusBox: CheckBox
        val dueDateText: TextView
        val editButton: ImageButton

        init {
            // Define click listener for the ViewHolder's View
            titleView = view.findViewById(R.id.taskItemTitle)
            descriptionView = view.findViewById(R.id.taskItemDescription)
            completedStatusBox = view.findViewById(R.id.taskItemCompleted)
            dueDateText = view.findViewById(R.id.taskItemDueDate)
            editButton = view.findViewById(R.id.taskItemEditAction)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        (viewHolder.titleView.text) = dataSet[position].title
        (viewHolder.descriptionView.text) = dataSet[position].description
        (viewHolder.completedStatusBox.isChecked) = dataSet[position].completed
        (viewHolder.completedStatusBox.buttonTintList) = ColorStateList.valueOf(dataSet[position].getPriorityColor())
        (viewHolder.dueDateText.text) = dataSet[position].getFormattedDate()
        (viewHolder.editButton.setOnClickListener { view ->
            viewHolder.taskListStore.setCurrentEdited(dataSet[position].uuid.toString())
            view.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        })
        (viewHolder.completedStatusBox.setOnCheckedChangeListener{ view, isChecked ->
            viewHolder.taskListStore.markAsCompleted(dataSet[position], isChecked)
        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
