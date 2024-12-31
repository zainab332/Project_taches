package com.example.project1

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.adapters.CategoryAdapter
import com.example.project1.classes.Category
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val timeTextView = view.findViewById<TextView>(R.id.timeTextView)
        val viewall = view.findViewById<TextView>(R.id.textView1)

        val currentDateTime = LocalDateTime.now()

        val dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM", Locale.getDefault())
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.getDefault())

        dateTextView.text = "${currentDateTime.format(dateFormatter)}"
        timeTextView.text = "${currentDateTime.format(timeFormatter)}"


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)

        val categories = listOf(
            Category(1, getString(R.string.Work), getString(R.string.WorkDesc), R.drawable.work),
            Category(2, getString(R.string.Personnel), getString(R.string.PersonnelDesc), R.drawable.travel),
            Category(3, getString(R.string.Hobby), getString(R.string.HobbyDesc), R.drawable.study)
        )

        val adapter = CategoryAdapter(requireContext(), categories) { category ->
            println("")
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val cardViewTask = view.findViewById<CardView>(R.id.cardViewTask)
        cardViewTask.setOnClickListener {
            cardViewTask.setBackgroundColor(R.color.blue)
            val secondFragment = TaskFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, secondFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()

        }

        val cardViewFavoriteTask = view.findViewById<CardView>(R.id.cardViewFavorite)
        cardViewFavoriteTask.setOnClickListener {
            cardViewFavoriteTask.setBackgroundColor(R.color.blue)

            val secondFragment = FavoriteTaskFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, secondFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()

        }

        val cardViewArchive = view.findViewById<CardView>(R.id.cardViewArchive)
        cardViewArchive.setOnClickListener {
            cardViewArchive.setBackgroundColor(R.color.blue)
            val secondFragment = ArchiveTaskFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, secondFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()

        }


       viewall.setOnClickListener{
            val secondFragment = CategoryFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, secondFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
