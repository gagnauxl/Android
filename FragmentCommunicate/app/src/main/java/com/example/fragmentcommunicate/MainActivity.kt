package com.example.fragmentcommunicate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity(), SimpleFragment.OnFragmentInteractionListener {
    val STATE_FRAGMENT = "state_of_fragment"
    private lateinit var button: Button
    private var isFragmentDisplayed = false
    private var radioButtonChoice = 2 // The default (no choice).

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.open_button)

        if (savedInstanceState != null) {
            isFragmentDisplayed =
                savedInstanceState.getBoolean(STATE_FRAGMENT)
            if (isFragmentDisplayed) {
                // If the fragment is displayed, change button to "close".
                button.setText(R.string.close)
            }
        }
    }

    // Implementation of OnFragmentInteractionListener
    override fun onRadioButtonChoice(choice: Int) {
        // Keep the radio button choice to pass it back to the fragment.
        radioButtonChoice = choice
        Log.d("Hexagon-LOG", "MainActivity onRadioButtonChoice subscriber: $choice")
        Toast.makeText(this, "Choice is: $choice", Toast.LENGTH_SHORT).show()
    }

    // Communication flow:
    // PASS parameters → CONSTRUCTOR
    // → store the parameter into the created bundle
    // → onCreateView restore parameters in the view from the bundle
    fun displayFragment() {
        Log.d("Hexagon-LOG", "MainActivity display Fragment: $radioButtonChoice")
        val simpleFragment = SimpleFragment(radioButtonChoice = this.radioButtonChoice)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager
            .beginTransaction()

        // Add the SimpleFragment.
        fragmentTransaction.add(
            R.id.fragment_container,
            simpleFragment
        ).addToBackStack(null).commit()
        // Update the Button text.
        button.setText(R.string.close)
        // Set boolean flag to indicate fragment is open.
        isFragmentDisplayed = true
    }

    fun closeFragment() {
        // Get the FragmentManager.
        val fragmentManager = supportFragmentManager
        // Check to see if the fragment is already showing.
        val simpleFragment = fragmentManager
            .findFragmentById(R.id.fragment_container) as SimpleFragment

        // Create and commit the transaction to remove the fragment.
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(simpleFragment).commit()

        // Update the Button text.
        button.setText(R.string.open)
        // Set boolean flag to indicate fragment is closed.
        isFragmentDisplayed = false
    }

    @Suppress("UNUSED_PARAMETER")
    fun onButtonClicked(view: View) {
        if (!isFragmentDisplayed) {
            displayFragment()
        } else {
            closeFragment()
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Save the state of the fragment (true=open, false=closed).
        savedInstanceState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed)
        super.onSaveInstanceState(savedInstanceState)
        Log.d("Hexagon-LOG", "MainActivity onSaveInstanceState: $radioButtonChoice")
    }
}
