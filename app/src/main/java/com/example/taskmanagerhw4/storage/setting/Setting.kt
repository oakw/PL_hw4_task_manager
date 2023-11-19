package com.example.taskmanagerhw4.storage.setting

import androidx.room.*

// Key-value pairs to help the application operate
@Entity(tableName = "settings")
data class Setting(
    @ColumnInfo(name = "Key") val key: String?,
    @ColumnInfo(name = "Value") val value: String?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int? = null
)
