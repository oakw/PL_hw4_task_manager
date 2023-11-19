package com.example.taskmanagerhw4.storage.task

import androidx.room.*

@Dao
interface TaskItemDao {
    @Query("SELECT * FROM task_item")
    fun getAll(): List<TaskItem>

    @Update
    fun update(task: TaskItem)

    @Insert
    fun insertAll(vararg users: TaskItem)

    @Query("DELETE FROM task_item WHERE UUId = :uuid")
    fun deleteUsingUUId(uuid: String)
}
