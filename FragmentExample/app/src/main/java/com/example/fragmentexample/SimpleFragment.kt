package com.example.fragmentexample

import android.media.Rating
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import android.util.Log
import android.widget.Toast


/**
 * A simple [Fragment] subclass.
 */
class SimpleFragment : Fragment() {
    private val YES = 0
    private val NO = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.fragment_simple, container, false)
        val radioGroup: RadioGroup = rootView.findViewById(R.id.radio_group)
        val ratingBar: RatingBar = rootView.findViewById(R.id.ratingBar)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = radioGroup.findViewById<View>(checkedId)
            val index = radioGroup.indexOfChild(radioButton)
            val textView: TextView = rootView.findViewById(R.id.fragment_header)
            when (index) {
                YES // User chose "Yes."
                -> textView.setText(R.string.yes_message)
                NO // User chose "No."
                -> textView.setText(R.string.no_message)
                else // No choice made.
                -> {
                }
            }// Do nothing.
        }

        // Set the rating bar onCheckedChanged listener.
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                // Get rating and show Toast with rating.
                val myRating = getString(R.string.my_rating) + ratingBar.rating.toString()
                Toast.makeText(
                    context, myRating,
                    Toast.LENGTH_SHORT
                ).show()
            }

//        // Inflate the layout for this fragment
        return rootView
    }

    companion object {
        fun newInstance(): SimpleFragment {
            return SimpleFragment()
        }
    }
}
