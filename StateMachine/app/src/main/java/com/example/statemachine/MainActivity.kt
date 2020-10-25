package com.example.statemachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName

    enum class State {
        DISCONNECTED,
        HOME, WORKING, RESULT  // Connected
    }

    enum class Event {
        CONNECT, DISCONNECT, COMMAND, RESPONSE, BACK
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: starting..")


        // 1-dimensional
        var e: Event = Event.COMMAND
        val arr = Array<Event>(size = 5) { Event.CONNECT }
//        val arr2:Array<Event> = Array<Event>(size=5, init=Event.CONNECT)
        arr[0] = e  // array starts with 0

        val t = Event.values()
        val aa = intArrayOf(67, 8, 9)
        val arr2 = arrayOf<Event>(Event.CONNECT, Event.DISCONNECT, Event.COMMAND)
        val arrl = listOf<Event>(Event.CONNECT, Event.DISCONNECT, Event.COMMAND)
        e = arr2[1]


        // 2-dimensional array
        val arr2d = Array(3, { IntArray(4) })
        val arr2d2 = arrayOf<Array<State>>(
                arrayOf<State>(State.DISCONNECTED, State.HOME),
                arrayOf<State>(State.WORKING, State.RESULT)
        )
        val s: State=arr2d2[0][1]
        val s2: State=arr2d2[1][1]
        val i: Int = e.ordinal

        // Next State Map
        val nextState = arrayOf<Array<State>>(
                //             CONNECT,         DISCONNECT,         COMMAND,          RESPONSE,             BACK
                arrayOf<State>(State.HOME, State.DISCONNECTED, State.DISCONNECTED, State.DISCONNECTED, State.DISCONNECTED), // DISCONNECTED
                arrayOf<State>(State.HOME, State.DISCONNECTED, State.WORKING,      State.RESULT,      State.HOME),          // HOME
                arrayOf<State>(State.HOME, State.DISCONNECTED, State.WORKING,      State.RESULT,      State.HOME),          // WORKING
                arrayOf<State>(State.HOME, State.DISCONNECTED, State.WORKING,      State.RESULT,      State.HOME)           // RESULT
        )

        val state1: State = nextState[State.DISCONNECTED.ordinal][Event.CONNECT.ordinal]  // HOME
        val state2: State = nextState[State.HOME.ordinal][Event.COMMAND.ordinal]    // WORKING
        val state3: State = nextState[State.RESULT.ordinal][Event.BACK.ordinal]  // HOME
        val event: Event = Event.COMMAND
        val state4: State = nextState [state3.ordinal][event.ordinal]            // WORKING

        val actionTable = arrayOf<()->Unit>(
            { Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: action1")},
            { Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: action2")},
            {action3()}
        )
        actionTable[0]()
        actionTable[Event.COMMAND.ordinal]()
    }

    private fun action3() {
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: action3")
    }
}
