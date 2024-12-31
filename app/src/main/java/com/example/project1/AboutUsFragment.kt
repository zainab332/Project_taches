package com.example.project1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutUsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutUsFragment : Fragment() {
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

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)


        val textViewGit: TextView = view.findViewById(R.id.textWithLinkgit)
        textViewGit.text = Html.fromHtml(
            "<a href='https://github.com/Wissal058/Test123.git'>GitHub</a>",
            Html.FROM_HTML_MODE_LEGACY
        )
        textViewGit.movementMethod = LinkMovementMethod.getInstance()

        val textViewFb: TextView = view.findViewById(R.id.textWithLinkfeecbok)
        textViewFb.text = Html.fromHtml(
            "<a href='https://github.com/Wissal058/Test123.git'>Facebook</a>",
            Html.FROM_HTML_MODE_LEGACY
        )

        textViewFb.movementMethod = LinkMovementMethod.getInstance()

        val textViewInst: TextView = view.findViewById(R.id.textWithLinkinstagram)
        textViewInst.text = Html.fromHtml(
            "<a href='https://www.instagram.com/wis_sal58/profilecard/?igsh=MWJkejhtMnJvaHFxcg=='>Instagram</a>",
            Html.FROM_HTML_MODE_LEGACY
        )
        textViewInst.movementMethod = LinkMovementMethod.getInstance()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutUsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutUsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}