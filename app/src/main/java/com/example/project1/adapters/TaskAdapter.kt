package com.example.project1.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.classes.Task

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onItemClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDateLimit: TextView = itemView.findViewById(R.id.task_dateLimit)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_items, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.title
        holder.taskDateLimit.text = task.dueDate.toString()
        holder.favoriteIcon.isEnabled = !task.isArchived
        holder.favoriteIcon.setImageResource(
            if (task.isFavorite) R.drawable.is_favorite else R.drawable.favorite0
        )

        holder.itemView.setOnClickListener { onItemClick(task) }

        holder.favoriteIcon.setOnClickListener {
            task.isFavorite = !task.isFavorite
            notifyItemChanged(position) // Met à jour uniquement l'élément
        }
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.task_checkbox)
        checkBox.isChecked = task.isCompleted

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (task.isCompleted != isChecked) { // Évitez les appels redondants
                task.isCompleted = isChecked
                if (isChecked) {
                    showCompletionDialog(holder.itemView.context, task.title)
                }
            }
        }

        holder.itemView.setOnClickListener { onItemClick(task) }

        holder.favoriteIcon.setOnClickListener {
            task.isFavorite = !task.isFavorite
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    fun removeTaskAtPosition(position: Int): Task? {
        return if (position in tasks.indices) {
            val removedTask = tasks.removeAt(position)
            notifyItemRemoved(position)
            removedTask
        } else {
            null
        }
    }


    val currentTasks: List<Task>
        get() = tasks

    @SuppressLint("MissingInflatedId")
    private fun showCompletionDialog(context: Context, taskTitle: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.terminee, null)
        val ok = dialogView.findViewById<Button>(R.id.ok)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Fond transparent
            setGravity(Gravity.CENTER) // Centrer le dialogue à l'écran
        }
        dialog.show()
        ok.setOnClickListener{
            dialog.dismiss()
        }

    }


    fun getTaskAtPosition(position: Int): Task? {
        return if (position in tasks.indices) {
            tasks[position]
        } else {
            null
        }
    }

}
