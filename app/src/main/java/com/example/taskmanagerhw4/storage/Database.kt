package com.example.taskmanagerhw4.storage

import androidx.room.*
import com.example.taskmanagerhw4.storage.setting.Setting
import com.example.taskmanagerhw4.storage.setting.SettingDao
import com.example.taskmanagerhw4.storage.task.TaskItem
import com.example.taskmanagerhw4.storage.task.TaskItemDao

// Database implementation using Android Room Library
@Database(entities = [TaskItem::class, Setting::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskItemDao(): TaskItemDao
    abstract fun settingDao(): SettingDao
}
