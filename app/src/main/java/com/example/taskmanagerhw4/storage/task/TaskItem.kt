package com.example.taskmanagerhw4.storage.task

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Entity(tableName = "task_item")
data class TaskItem(
    @ColumnInfo(name = "Title") var title: String,
    @ColumnInfo(name = "Description") var description: String,
    @ColumnInfo(name = "DueDate") var dueDate: String,
    @ColumnInfo(name = "PriorityLevel") var priorityLevel: Int,
    @ColumnInfo(name = "IsCompleted") var completed: Boolean = false,
    @PrimaryKey @ColumnInfo(name = "UUId") val uuid: String = UUID.randomUUID().toString()
) {


    // Different priorities are denoted by using different colors
    fun getPriorityColor(): Int {
        return getPriorityColor(this.priorityLevel)
    }


    // Due date is shown just as DD.MM
    fun getFormattedDate(): String {
        val date = LocalDate.parse(this.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        return "${date.dayOfMonth}.${date.monthValue}"
    }


    override fun toString(): String {
        return Gson().toJson(this)
    }


    // With help by:
    // https://stackoverflow.com/questions/40352684/what-is-the-equivalent-of-java-static-methods-in-kotlin
    companion object {
        fun fromJson(json: String): TaskItem {
            return Gson().fromJson(json, TaskItem::class.java)
        }

        fun getPriorityColor(priorityLevel: Int): Int {
            return Color.parseColor(when (priorityLevel) {
                0 -> "#4CAF50"
                1 -> "#FF9800"
                else -> "#FF0000"
            })
        }
    }

}
