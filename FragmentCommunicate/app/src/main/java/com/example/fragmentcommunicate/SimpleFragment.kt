package com.example.fragmentcommunicate

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.fragment_simple.*

/**
 * A simple [Fragment] subclass.
 */
class SimpleFragment() : Fragment() {
    private val YES = 0
    private val NO = 1
    private val NONE = 2
    var radioButtonChoice = NONE
    private val CHOICE = "choice"

    var rating : Float = 0f
    private val RATING = "rating"

    internal interface OnFragmentInteractionListener {
        fun onRadioButtonChoice(choice: Int)
    }

    constructor(radioButtonChoice: Int) : this() {
        this.radioButtonChoice = radioButtonChoice
        val arguments = Bundle()
        arguments.putInt(CHOICE, radioButtonChoice)
        setArguments(arguments)
    }

    private lateinit var listener: OnFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.fragment_simple, container, false)
        val radioGroup: RadioGroup = rootView.findViewById(R.id.radio_group)
        val ratingBar: RatingBar = rootView.findViewById(R.id.ratingBar)

        // read setting from the bundle and sets it
        if (getArguments()!!.containsKey(CHOICE)) {
            // A choice was made, so get the choice.
            radioButtonChoice = getArguments()!!.getInt(CHOICE);
            // Check the radio button choice.
            if (radioButtonChoice != NONE) {
                radioGroup.check(
                    radioGroup.getChildAt(radioButtonChoice).getId()
                );
            }
        }
        if (getArguments()!!.containsKey(RATING)) {
            // A rating was made, so get the rating
            rating = getArguments()!!.getFloat(RATING);
            // set the rating.
            ratingBar.rating = this.rating
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = radioGroup.findViewById<View>(checkedId)
            val index = radioGroup.indexOfChild(radioButton)
            val textView: TextView = rootView.findViewById(R.id.fragment_header)

            when (index) {
                YES -> {
                    textView.setText(R.string.yes_message)
                    radioButtonChoice = YES
                }
                NO -> {
                    textView.setText(R.string.no_message)
                    radioButtonChoice = NO
                }
                else -> {
                    radioButtonChoice = NONE
                }
            }

            // store the change in the bundle, then no need to do it in the constructor
            // in addition it survives a config change
            // not workging to be analyzed
            val arguments = getArguments()
            arguments?.putInt(CHOICE, radioButtonChoice)
            setArguments(arguments)

            // publish change
            listener.onRadioButtonChoice(index);
        }

        // Set the rating bar onCheckedChanged listener.
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                // Get rating and show Toast with rating.
                val myRating = "${getString(R.string.my_rating)} ${ratingBar.rating}"
                Toast.makeText(
                    context, myRating,
                    Toast.LENGTH_SHORT
                ).show()

                val arguments = arguments
                arguments?.putFloat(RATING, rating)
                setArguments(arguments)
            }

        // Inflate the layout for this fragment
        return rootView
    }


    // attach a listener to the context, so that other fragments or apps can subscribe
    // to this listener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + resources.getString(R.string.exception_message))
        }
    }

}
