package com.example.project1.classes

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.Date

data class Task @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: Int,
    var title: String,
    var description: String? = null,
    var isCompleted: Boolean = false,
    var priority: TaskPriority = TaskPriority.MEDIUM,
    var dueDate: LocalDate? = null,
    @SuppressLint("NewApi") var creationDate: LocalDate = LocalDate.now(),
    var category: Category,
    var status: TaskStatus = TaskStatus.Pending,
    var isFavorite: Boolean,
    var isArchived: Boolean
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
