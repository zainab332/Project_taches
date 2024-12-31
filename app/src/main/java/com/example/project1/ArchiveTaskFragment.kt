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
import android.widget.ImageView
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
import com.example.project1.classes.SwipeGestureArchive

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArchiveTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArchiveTaskFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val taskViewModel: TaskViewModel by activityViewModels()
    private lateinit var adapterTaskArchive: TaskAdapter
    private lateinit var searchBarArchive: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_archive_task, container, false)
        val archive_img = view.findViewById<LinearLayout>(R.id.img_vide)
        val texteAr = view.findViewById<TextView>(R.id.tsk)

        // Initialisation de la RecyclerView
        val recyclerViewTask: RecyclerView = view.findViewById(R.id.recyclerViewArchiveTask)
        recyclerViewTask.layoutManager = LinearLayoutManager(requireContext())

        // Initialisation de l'adaptateur
        adapterTaskArchive = TaskAdapter(mutableListOf()) { task ->
            val detailFragment = TaskDetailFragment.newInstance(task.id)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, detailFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerViewTask.adapter = adapterTaskArchive


        val swipeGestureArch = object : SwipeGestureArchive(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapterTaskArchive.getTaskAtPosition(position)

                if (task != null) {
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            adapterTaskArchive.removeTaskAtPosition(position) // Retirer de l'adaptateur
                            taskViewModel.removeTaskArch(task) // Retirer de la liste principale
                            Toast.makeText(context, getString(R.string.TaskDeleted) + " : ${task.title}", Toast.LENGTH_SHORT).show()
                        }
                        ItemTouchHelper.RIGHT -> {
                            adapterTaskArchive.removeTaskAtPosition(position)
                            taskViewModel.unarchiveTask(task)
                            Toast.makeText(context, getString(R.string.TaskDesArchived) + " : ${task.title}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeGestureArch)
        itemTouchHelper.attachToRecyclerView(recyclerViewTask)

        // Observer la liste des tâches
        taskViewModel.taskArch.observe(viewLifecycleOwner) { archivedTasks ->
            adapterTaskArchive.updateTasks(archivedTasks)
            if (archivedTasks.isEmpty()) {
                archive_img.visibility = View.VISIBLE
                texteAr.visibility = View.GONE
            } else {
                archive_img.visibility = View.GONE
                texteAr.visibility = View.VISIBLE
            }
        }

        // Initialisation de la barre de recherche
        searchBarArchive = view.findViewById(R.id.searchArchive)
        searchBarArchive.addTextChangedListener { text ->
            val query = text.toString().lowercase()
            val filteredList = adapterTaskArchive.currentTasks.filter {
                it.title.lowercase().contains(query) || (it.description?.lowercase()?.contains(query) ?: false)
            }
            adapterTaskArchive.updateTasks(filteredList)
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
         * @return A new instance of fragment ArchiveTaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArchiveTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}