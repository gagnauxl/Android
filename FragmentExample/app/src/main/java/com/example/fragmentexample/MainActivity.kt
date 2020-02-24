package com.example.fragmentexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {
    val STATE_FRAGMENT = "state_of_fragment"
    private lateinit var button: Button
    private var isFragmentDisplayed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.open_button);

        if (savedInstanceState != null) {
            isFragmentDisplayed =
                savedInstanceState.getBoolean(STATE_FRAGMENT);
            if (isFragmentDisplayed) {
                // If the fragment is displayed, change button to "close".
                button.setText(R.string.close);
            }
        }
    }

    fun displayFragment() {
//        val simpleFragment =  SimpleFragment.newInstance()
        val simpleFragment = SimpleFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager
            .beginTransaction()

        // Add the SimpleFragment.
        fragmentTransaction.add(
            R.id.fragment_container,
            simpleFragment
        ).addToBackStack(null).commit()
        // Update the Button text.
        button.setText(R.string.close);
        // Set boolean flag to indicate fragment is open.
        isFragmentDisplayed = true;
    }

    fun closeFragment() {
        // Get the FragmentManager.
        val fragmentManager = supportFragmentManager
        // Check to see if the fragment is already showing.
        val simpleFragment = fragmentManager
            .findFragmentById(R.id.fragment_container) as SimpleFragment
        if (simpleFragment != null) {
            // Create and commit the transaction to remove the fragment.
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(simpleFragment).commit()
        }
        // Update the Button text.
        button.setText(R.string.open)
        // Set boolean flag to indicate fragment is closed.
        isFragmentDisplayed = false
    }

    fun onButtonClicked(view: View) {
        if (!isFragmentDisplayed) {
            displayFragment();
        } else {
            closeFragment();
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Save the state of the fragment (true=open, false=closed).
        savedInstanceState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed)
        super.onSaveInstanceState(savedInstanceState)
    }
}
