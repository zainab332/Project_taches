package com.example.project1

import TaskViewModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.project1.R
import com.example.project1.classes.Task
import com.example.project1.classes.TaskPriority
import java.time.LocalDate

class TaskDetailFragment : Fragment() {

    private val taskViewModel: TaskViewModel by activityViewModels()

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerPriority: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var textViewCreationDate: TextView
    private lateinit var editTextDueDate: TextView
    private lateinit var checkBoxFavorite: CheckBox
    private lateinit var btnEdit: LinearLayout
    private lateinit var btnSave: Button
    private lateinit var calendarEdit : ImageButton
    private lateinit var close : ImageButton

    private var task: Task? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_detail2_mod, container, false)

        editTextTitle = view.findViewById(R.id.editTextTitle)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        spinnerPriority = view.findViewById(R.id.spinnerPriority)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)
        textViewCreationDate = view.findViewById(R.id.textViewCreationDate)
        editTextDueDate = view.findViewById(R.id.editTextDueDate)
        checkBoxFavorite = view.findViewById(R.id.checkBoxFavorite)
        btnEdit = view.findViewById(R.id.btnEdit)
        btnSave = view.findViewById(R.id.btnSave)
        calendarEdit = view.findViewById(R.id.calendarEdit)
        close = view.findViewById(R.id.close)
        // Remplir les priorités dans le Spinner
        spinnerPriority.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, TaskPriority.values()
        )

        arguments?.let {
            val taskId = it.getInt(ARG_TASK_ID)
            task = taskViewModel.tasks.value?.find { task -> task.id == taskId }
        }

        // Observer les catégories dans le ViewModel pour remplir le Spinner
        taskViewModel.categories.observe(viewLifecycleOwner) { categories ->
            val categoryTitles = taskViewModel.categories.value!!.map { it.title }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryTitles
            )
            spinnerCategory.adapter = adapter

            task?.let { task ->
                val selectedIndex = categories.indexOfFirst { it.id == task.category.id }
                if (selectedIndex != -1) {
                    spinnerCategory.setSelection(selectedIndex)
                }
            }
        }

        task?.let { task ->
            editTextTitle.setText(task.title)
            editTextDescription.setText(task.description)
            spinnerPriority.setSelection(task.priority.ordinal)
            textViewCreationDate.text = "Date de création : ${task.creationDate}"
            editTextDueDate.setText(task.dueDate?.toString())
            checkBoxFavorite.isChecked = task.isFavorite
        }

        calendarEdit.setOnClickListener {
            showDatePickerDialog()
        }

        // Activer la modification
        btnEdit.setOnClickListener {
            enableFields(true)
            btnSave.visibility = View.VISIBLE
            btnEdit.visibility = View.GONE
        }

        // Sauvegarder les modifications
        btnSave.setOnClickListener {
            task?.let { task ->
                task.title = editTextTitle.text.toString()
                task.description = editTextDescription.text.toString()
                task.priority = TaskPriority.values()[spinnerPriority.selectedItemPosition]
                task.dueDate = editTextDueDate.text.toString().takeIf { it.isNotEmpty()  }
                    ?.let { LocalDate.parse(it) }
                task.category = taskViewModel.categories.value?.get(spinnerCategory.selectedItemPosition) ?: task.category
                task.isFavorite = checkBoxFavorite.isChecked

                taskViewModel.updateTask(task) // Mettre à jour dans le ViewModel
            }

            enableFields(false)
            btnSave.visibility = View.GONE
            btnEdit.visibility = View.VISIBLE
        }
        close.setOnClickListener{
            btnEdit.visibility = View.GONE
        }

        return view
    }
//Permet d'activer ou désactiver les champs en fonction de l'état de modification.
    private fun enableFields(enable: Boolean) {
        editTextTitle.isEnabled = enable
        editTextDescription.isEnabled = enable
        spinnerPriority.isEnabled = enable
        spinnerCategory.isEnabled = enable
        editTextDueDate.isEnabled = enable
        checkBoxFavorite.isEnabled = enable
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val today = LocalDate.now()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                editTextDueDate.setText("Due Date${ selectedDate.toString() }")
            },
            today.year,
            today.monthValue - 1,
            today.dayOfMonth
        )
        datePicker.show()
    }

    companion object {
        private const val ARG_TASK_ID = "task_id"

        fun newInstance(taskId: Int) = TaskDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_TASK_ID, taskId)
            }
        }
    }
}
