package com.example.taskmanagerhw4

import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun comparison_isCorrect() {
        val task1 = TaskItem("Sample Title 1", "A description of choice", "04.11.2023", 2)
        val task2 = TaskItem("Sample Title 2", "Op", "01.11.2023", 0)
        var sortedList = arrayOf(task1, task2).sortedWith(filterSelectorSort(SortType.DUE_DATE_DESC.toString()))
        assertEquals(2,  sortedList[0].priorityLevel)
    }



    fun filterSelectorSort(sortType: String): (TaskItem, TaskItem) -> Int {
        return fun(v1: TaskItem, v2: TaskItem) =
            (when(sortType) {
                    SortType.DUE_DATE_ASC.toString() -> { (LocalDate.parse(v1.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).toEpochDay() - LocalDate.parse(v2.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).toEpochDay()).toInt() }
                    SortType.DUE_DATE_DESC.toString() -> { (LocalDate.parse(v2.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).toEpochDay() - LocalDate.parse(v1.dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).toEpochDay()).toInt() }
                    SortType.TITLE_ASC.toString() -> { v1.title.compareTo(v2.title, true) }
                    SortType.TITLE_DESC.toString() -> { v2.title.compareTo(v1.title, true) }
                    SortType.PRIORITY_ASC.toString() -> { v1.priorityLevel - v2.priorityLevel }
                    SortType.PRIORITY_DESC.toString() -> { v2.priorityLevel - v1.priorityLevel }
                    else -> { if (v1.completed) 1 else -1 }
                })
    }
}