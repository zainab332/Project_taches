package com.example.project1.classes

sealed class TaskStatus {
    object Pending : TaskStatus()
    object InProgress : TaskStatus()
    object Completed : TaskStatus()
    data class Custom(val description: String) : TaskStatus() // Statut personnalisé
}
