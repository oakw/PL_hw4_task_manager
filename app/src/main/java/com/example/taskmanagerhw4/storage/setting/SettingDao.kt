package com.example.taskmanagerhw4.storage.setting

import androidx.room.*

@Dao
abstract class SettingDao {
    @Query("SELECT Value FROM settings WHERE Key = :key LIMIT 1")
    abstract fun getByKey(key: String): String?

    fun insertOrUpdateValue(key: String, value: String) {
        if (this.getByKey(key) == null) {
            this.insert(Setting(key, value))
        } else {
            this.updateValue(key, value)
        }
    }

    @Query("UPDATE settings SET Value = :value WHERE Key = :key")
    abstract fun updateValue(key: String, value: String)

    @Insert
    abstract fun insert(setting: Setting)

    @Query("DELETE FROM settings WHERE Key = :key")
    abstract fun deleteByKey(key: String)
}
