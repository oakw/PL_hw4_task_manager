package com.example.taskmanagerhw4

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter


// Performs operations with tasks
class TaskListStore(context: Context) {
    val sharedPreferences = context.getSharedPreferences("TaskList", Context.MODE_PRIVATE)
    val sharedPreferencesSettings = context.getSharedPreferences("TaskListSettings", Context.MODE_PRIVATE)
    var tasks: Array<TaskItem> = arrayOf()

    init {
        refreshAll()
    }

    fun initializeStore() {
        stopEditing()

        // Uncomment to get sample data at the beginning of app's lifecycle
        //  if (getAllTasks().isEmpty()) {
        //      addTask(TaskItem("Sample Title 1", "A description of choice", "04.11.2023", 2))
        //      addTask(TaskItem("Sample Title 2", "Op", "01.11.2023", 0))
        //      addTask(TaskItem("Sample Title 3", "Do this task on your own pace and submit to the professor", "01.12.2023", 1))
        //  }
    }


    /**
     * CRUD Operations with the task list
     */
    fun getAllTasks(): Array<TaskItem> {
        val sortType = sharedPreferencesSettings.getString("SortType", SortType.DUE_DATE_ASC.toString()) ?: SortType.DUE_DATE_ASC.toString()
        val a = tasks.sortedWith(filterSelectorSort(sortType))

        return tasks.sortedWith(filterSelectorSort(sortType)).toTypedArray()
    }


    fun addTask(task: TaskItem) {
        tasks = tasks.plus(task)
        sharedPreferences.edit().putString(task.uuid.toString(), task.toString()).apply()
    }


    fun updateTask(updatedTask: TaskItem) {
        val task = tasks.find { task -> task.uuid.toString() == updatedTask.uuid.toString() }
        task?.title = updatedTask.title
        task?.dueDate = updatedTask.dueDate
        task?.description = updatedTask.description
        task?.priorityLevel = updatedTask.priorityLevel
        task?.completed = updatedTask.completed
        sharedPreferences.edit().putString(updatedTask.uuid.toString(), updatedTask.toString()).apply()
    }


    fun deleteTask(uuid: String) {
        tasks = (tasks.filterNot { task -> task.uuid.toString() == uuid }).toTypedArray()
        sharedPreferences.edit().remove(uuid).apply()
    }


    fun markAsCompleted(task: TaskItem, completed: Boolean = true) {
        task.completed = completed
        updateTask(task)
    }


    /**
     * Editing.
     * Currently edited task details are preserved to correctly restore the state
     */
    fun setCurrentlyEdited(uuid: String) {
        sharedPreferencesSettings.edit().putString("CurrentlyEdited", uuid).apply()
    }


    fun getCurrentlyEdited(): TaskItem? {
        val editedUUId = sharedPreferencesSettings.getString("CurrentlyEdited", null)
        return editedUUId.let { tasks.find { task -> task.uuid.toString() == editedUUId } }
    }


    fun stopEditing() {
        sharedPreferencesSettings.edit().remove("CurrentlyEdited").apply()
    }


    // Retrieve all tasks from sharedPreferences to tasklist
    private fun refreshAll() {
        for (value in sharedPreferences.getAll().values) {
            tasks = tasks.plus(TaskItem.fromJson(value as String))
        }
    }


    // Save sorting setting in memory
    fun selectSortFilter(typeString: String) {
        val sortType = SortType.values().find { type -> type.toString() == typeString }
        sortType?.let {
            sharedPreferencesSettings.edit().putString("SortType", sortType.toString()).apply()
        }
    }


    // Return comparison function of two tasks based on sort type provided
    private fun filterSelectorSort(sortType: String): (TaskItem, TaskItem) -> Int {
        return fun(v1: TaskItem, v2: TaskItem) =
            (when(sortType) {
                SortType.DUE_DATE_ASC.toString() -> {
                    (LocalDate.parse(v1.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).toEpochDay()
                            - LocalDate.parse(v2.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).toEpochDay()).toInt()
                }
                SortType.DUE_DATE_DESC.toString() -> {
                    (LocalDate.parse(v2.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).toEpochDay()
                            - LocalDate.parse(v1.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).toEpochDay()).toInt()
                }
                SortType.TITLE_ASC.toString() -> { v1.title.compareTo(v2.title, true) }
                SortType.TITLE_DESC.toString() -> { v2.title.compareTo(v1.title, true) }
                SortType.PRIORITY_ASC.toString() -> { v1.priorityLevel - v2.priorityLevel }
                SortType.PRIORITY_DESC.toString() -> { v2.priorityLevel - v1.priorityLevel }
                else -> { if (v1.completed) 1 else -1 }
            })
    }
}