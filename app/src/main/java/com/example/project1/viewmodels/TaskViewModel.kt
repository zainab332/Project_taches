import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.project1.R
import com.example.project1.classes.Category
import com.example.project1.classes.Task
import com.example.project1.classes.TaskPriority
import java.util.Date
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
class TaskViewModel : ViewModel() {
    val tasks: MutableLiveData<MutableList<Task>> = MutableLiveData(mutableListOf())
    val categories: MutableLiveData<MutableList<Category>> = MutableLiveData(mutableListOf())
    val taskFav: MutableLiveData<MutableList<Task>> = MutableLiveData(mutableListOf())
    val taskArch: MutableLiveData<MutableList<Task>> = MutableLiveData(mutableListOf())

    init {
        categories.value = mutableListOf(
            Category(1,"Work", "Scheduale, Agenda...", R.drawable.work),
            Category(2,"Personal", "Family events...", R.drawable.travel),
            Category(3, "Hobby", "Reading, Skydiving...", R.drawable.study)
        )
        tasks.value = mutableListOf(
            Task(0, "Projet des interfaces", isCompleted = false, priority = TaskPriority.HIGH, category = categories.value!![0], creationDate = LocalDate.of(2024, 12, 1), isFavorite = true, isArchived = false, dueDate = LocalDate.of(2025, 12, 12)),
            Task(1, "Projet des interfaces", isCompleted = true, priority = TaskPriority.HIGH, category = categories.value!![0], creationDate = LocalDate.of(2024, 12, 11), isFavorite = true, isArchived = false, dueDate = LocalDate.of(2025, 12, 14)),
            Task(2, "Projet des interfaces", isCompleted = true, priority = TaskPriority.HIGH, category = categories.value!![0], creationDate = LocalDate.of(2024, 12, 21), isFavorite = true, isArchived = false, dueDate = LocalDate.of(2025, 12, 15)),
            Task(3, "Projet des interfaces", isCompleted = false, priority = TaskPriority.HIGH, category = categories.value!![0], creationDate = LocalDate.of(2024, 12, 31), isFavorite = true, isArchived = false, dueDate = LocalDate.of(2025, 12, 30)),
        )
    }

    fun addTask(task: Task) {
        tasks.value?.add(task)
        tasks.notifyChange()
    }

    fun removeTask(task: Task) {
        val mainTasks = tasks.value ?: return
        val updatedTasks = mainTasks.toMutableList().apply { remove(task) }
        tasks.value = updatedTasks
    }
    fun removeTaskArch(task: Task) {
        val mainTasks = taskArch.value ?: return
        val updatedTasks = mainTasks.toMutableList().apply { remove(task) }
        taskArch.value = updatedTasks
    }

    fun archiveTask(task: Task) {
        val mainTasks = tasks.value ?: return
        val archivedTasks = taskArch.value ?: mutableListOf()

        // Trouver et archiver la tâche
        val taskToArchive = mainTasks.find { it.id == task.id } ?: return

        val updatedTasks = mainTasks.toMutableList().apply { remove(taskToArchive) }
        val updatedArchivedTasks = archivedTasks.toMutableList().apply {
            add(taskToArchive.copy(isArchived = true))
        }

        // Mettre à jour les LiveData
        tasks.value = updatedTasks
        taskArch.value = updatedArchivedTasks
        println("Task archived: ${task.title}")
    }

    fun unarchiveTask(task: Task) {
        val archivedTasks = taskArch.value ?: mutableListOf()
        val mainTasks = tasks.value ?: mutableListOf()

        // Trouver la tâche dans les archives
        val archivedTask = archivedTasks.find { it.id == task.id }

        if (archivedTask != null) {
            val updatedArchivedTasks = archivedTasks.toMutableList().apply { remove(archivedTask) }
            taskArch.value = updatedArchivedTasks

            val updatedMainTasks = mainTasks.toMutableList().apply {
                add(archivedTask.copy(isArchived = false))
            }
            tasks.value = updatedMainTasks

            println("Task unarchived: ${task.title}")
        } else {
            println("Erreur : Tâche introuvable pour désarchivage.")
        }
    }



    fun getTasksSize(): Int {
        return tasks.value?.size ?: 0
    }

    fun getCategorySize(): Int {
        return categories.value?.size ?: 0
    }

    fun markAsFavorite(task: Task) {
        tasks.value?.remove(task)
        taskFav.value = taskFav.value?.apply {
            add(task.copy(isFavorite = true))
        } ?: mutableListOf(task.copy(isFavorite = true))
        tasks.notifyChange()
        taskFav.notifyChange()
    }


    fun updateTask(task: Task) {
        when {
            task.isArchived -> {
                taskArch.value?.find { it.id == task.id }?.apply {
                    title = task.title
                    description = task.description
                    isFavorite = task.isFavorite
                    isArchived = task.isArchived
                }
                taskArch.notifyChange()
            }
            task.isFavorite -> {
                taskFav.value?.find { it.id == task.id }?.apply {
                    title = task.title
                    description = task.description
                    isFavorite = task.isFavorite
                    isArchived = task.isArchived
                }
                taskFav.notifyChange()
            }
            else -> {
                tasks.value?.find { it.id == task.id }?.apply {
                    title = task.title
                    description = task.description
                    isFavorite = task.isFavorite
                    isArchived = task.isArchived
                }
                tasks.notifyChange()
            }
        }
    }
    fun addCategory(category: Category) {
        categories.value?.add(category)
        categories.notifyChange()
    }
    fun getTasksByCategory(category: Category): List<Task> {
        return tasks.value?.filter { it.category.title == category.title } ?: emptyList()
    }

    fun getTasksForDate(date: LocalDate): List<Task> {
        return tasks.value?.filter { it.dueDate == date } ?: emptyList()
    }

}


fun <T> MutableLiveData<MutableList<T>>.notifyChange() {
    this.value = this.value?.toMutableList()// Pas besoin de recréer la liste à chaque fois
}




