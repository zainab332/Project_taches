package com.example.project1

import TaskViewModel
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.adapters.TaskAdapter
import com.example.project1.classes.Task
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val taskViewModel: TaskViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskListTitle: TextView
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val calendarView: CalendarView = view.findViewById(R.id.calendarView)
        recyclerView = view.findViewById(R.id.recyclerViewDate)
        taskListTitle = view.findViewById(R.id.textViewTitle)

        taskAdapter = TaskAdapter(mutableListOf()) { task ->
            println("Tâche sélectionnée : ${task.title}")
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = taskAdapter

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            val tasksForDate = taskViewModel.getTasksForDate(selectedDate)
            println("Tâches pour la date $selectedDate : ${tasksForDate.size}")
            updateUIWithTasks(tasksForDate)
        }



        return view
    }

    private fun updateUIWithTasks(tasks: List<Task>) {
        taskAdapter.updateTasks(tasks)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTaskListDate() {
        // Filtrer les tâches pour la date sélectionnée
        val filteredTasks = taskViewModel.tasks.value?.filter { task ->
            task.dueDate.toString() == selectedDate // Comparaison simple avec LocalDate
        } ?: emptyList()

        // Mettre à jour le RecyclerView
        taskAdapter.updateTasks(filteredTasks.toMutableList())

        // Mettre à jour le titre
        taskListTitle.text = "Tasks for $selectedDate"
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubscriptionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}