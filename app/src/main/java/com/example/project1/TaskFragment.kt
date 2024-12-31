package com.example.project1

import TaskViewModel
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.adapters.TaskAdapter
import com.example.project1.classes.SwipeGesture
import com.example.project1.classes.Task

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val taskViewModel: TaskViewModel by activityViewModels()
    private val archivedTasks = mutableListOf<Task>()
    private lateinit var adapterTask: TaskAdapter
    private lateinit var searchBar: EditText
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_all_task, container, false)
        val planning_img = view.findViewById<LinearLayout>(R.id.img_vide_task)
        val texteList = view.findViewById<TextView>(R.id.tskList)

        val recyclerViewTask: RecyclerView = view.findViewById(R.id.recyclerViewTaskAllWissal)
        recyclerViewTask.layoutManager = LinearLayoutManager(requireContext())


        adapterTask = TaskAdapter(mutableListOf()) { task ->
            val detailFragment = TaskDetailFragment.newInstance(task.id)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerViewTask.adapter = adapterTask

        // Observer les modifications dans la liste des tâches depuis le ViewModel
        taskViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapterTask.updateTasks(tasks)
            if (tasks.isEmpty()) {
                planning_img.visibility = View.VISIBLE
                texteList.visibility = View.GONE
            }
        }

        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapterTask.getTaskAtPosition(position)

                if (task != null) {
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            adapterTask.removeTaskAtPosition(position) // Supprimer de l'adaptateur
                            taskViewModel.removeTask(task) // Supprimer du ViewModel
                            Toast.makeText(context, getString(R.string.TaskDeleted) + " : ${task.title}", Toast.LENGTH_SHORT).show()
                        }
                        ItemTouchHelper.RIGHT -> {
                            adapterTask.removeTaskAtPosition(position)
                            taskViewModel.archiveTask(task)
                            Toast.makeText(context, getString(R.string.TaskArchived) + " : ${task.title}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(recyclerViewTask)

        taskViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapterTask.updateTasks(tasks)
        }

        searchBar = view.findViewById(R.id.search)
        searchBar.addTextChangedListener { text ->
            val query = text.toString().lowercase()
            val filteredList = taskViewModel.tasks.value?.filter {
                it.title.lowercase().contains(query) || (it.description?.lowercase()?.contains(query) ?: false)
            } ?: emptyList()
            adapterTask.updateTasks(filteredList)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}