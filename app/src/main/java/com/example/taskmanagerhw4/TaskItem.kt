package com.example.taskmanagerhw4

import android.graphics.Color
import android.util.JsonReader
import com.google.gson.*
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TaskItem(
    var title: String,
    var description: String,
    var dueDate: String,
    var priorityLevel: Int
) {
    var completed: Boolean = false
    val uuid = UUID.randomUUID()


    fun getPriorityColor(): Int {
        return getPriorityColor(this.priorityLevel)
    }

    fun getFormattedDate(): String {
        val date = LocalDate.parse(this.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        return "${date.dayOfMonth}.${date.monthValue}"
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    //    https://stackoverflow.com/questions/40352684/what-is-the-equivalent-of-java-static-methods-in-kotlin
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


//https://stackoverflow.com/questions/39192945/serialize-java-8-localdate-as-yyyy-mm-dd-with-gson
class LocalDateTypeAdapter : TypeAdapter<LocalDate>() {

    override fun write(out: JsonWriter, value: LocalDate) {
        out.value(DateTimeFormatter.ISO_LOCAL_DATE.format(value))
    }

    override fun read(input: com.google.gson.stream.JsonReader?): LocalDate {
        return LocalDate.parse(input!!.nextString())
    }
}