package com.example.project1

import TaskViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.adapters.TaskAdapter

class CategoryItemFragment : Fragment() {
    private val taskViewModel: TaskViewModel by activityViewModels()
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = it.getString(ARG_CATEGORY_NAME) // Initialiser categoryName
        }
    }

    @SuppressLint("MissingInflatedId", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category_item, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerCategoryTasks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = TaskAdapter(mutableListOf()) { updatedTask ->
            taskViewModel.updateTask(updatedTask) // Passer par le ViewModel
        }
        recyclerView.adapter = adapter

        categoryName?.let { name ->
            val category = taskViewModel.categories.value?.find { it.title == name }
            if (category != null) {
                val tasks = taskViewModel.getTasksByCategory(category)
                adapter.updateTasks(tasks)
            }
        }

        return view
    }

    companion object {
        private const val ARG_CATEGORY_NAME = "categoryName"

        fun newInstance(categoryName: String) = CategoryItemFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CATEGORY_NAME, categoryName)
            }
        }
    }
}
