package com.roche.hexagon.im.common.statemachine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.Exception

class Condition(val conditionFunction: () -> Boolean, val name: String) {
  fun evaluate() : Boolean {
    return conditionFunction.invoke()
  }
}

class Action(val actionFunction: () -> Unit, val name: String) {
  fun execute() {
    actionFunction.invoke()
  }
}

class State<STATE_ID : Enum<STATE_ID>, TRIGGER_ID : Enum<TRIGGER_ID>>(val stateID: STATE_ID, var entry: Action? = null, var exit: Action? = null) {
  val name: String = stateID.toString()
  private val transitions= mutableMapOf<TRIGGER_ID, Transition<STATE_ID, TRIGGER_ID>>()
  private var started = false

  fun evaluateTransition(trigger : TRIGGER_ID) : Boolean {
    val transition = transitions[trigger]
    if (transition != null) {
      return transition.evaluate()
    }
    return false
  }

  fun targetState(triggerId: TRIGGER_ID): STATE_ID? {
    val transition = transitions[triggerId]
    if (transition != null) {
      return transition.getTarget()
    }
    return null
  }

  fun triggerOnSubStateMachine(trigger : TRIGGER_ID) {
    if (subStateMachine != null) {
      subStateMachine?.trigger(trigger)
    }
  }

  fun setup(subStateMachine : StateMachine<STATE_ID, TRIGGER_ID>) {
    subStateMachine.subSM = true
    this.subStateMachine = subStateMachine
  }

  fun start() {
    if (entry != null) {
      entry?.execute()
    }
    if (subStateMachine != null) {
      subStateMachine?.start()
    }
    started = true
  }

  fun stop() {
    if (subStateMachine != null) {
      subStateMachine?.stop()
    }
    if (exit != null) {
      exit?.execute()
    }
    started = false
  }

  internal fun executeTransitionAction(triggerId: TRIGGER_ID) {
    transitions[triggerId]?.execute()
  }

  fun currentDeepState() : STATE_ID {
    return subStateMachine?.currentDeepState() ?: stateID
  }

  fun puml(actualTrigger : TRIGGER_ID?) : String {
    var retVal = "state $name\n"
    if (entry != null) {
      retVal += "$name : + entry: ${entry?.name}\n"
    }
    if (exit != null) {
      retVal += "$name : + exit: ${exit?.name}\n"
    }
    if (subStateMachine != null) {
      retVal += "\n" + subStateMachine?.puml()
    }
    for (transition in transitions) {
      retVal += "\n" + transition.value.puml(actualTrigger)
    }
    return retVal
  }

  internal fun add(transition : Transition<STATE_ID, TRIGGER_ID>) {
    if (transitions.containsKey(transition.triggerID)) {
      throw Exception("Already existing transition entry: $transition")
    }
    transitions[transition.triggerID] = transition
  }

  private var subStateMachine : StateMachine<STATE_ID, TRIGGER_ID>? = null
}

class Choice<STATE_ID : Enum<STATE_ID>>(val trueState: STATE_ID, val falseState: STATE_ID, val condition : Condition) {
  fun getTarget(): STATE_ID {
    return if (condition.evaluate()) {
      lastPath = true
      trueState
    } else {
      lastPath = false
      falseState
    }
  }

  fun puml(choiceName: String, activeTransition: Boolean) : String {
    if (activeTransition)
    {
      if (lastPath) {
        truePath = "-[#Red]->"
      } else {
        falsePath = "-[#Red]->"
      }
    }
    var retVal = "\n$choiceName $truePath $trueState : [${condition.name}]\n"
    retVal += "$choiceName $falsePath $falseState : [not ${condition.name}]\n"
    return retVal
  }

  private var lastPath = false
  private var truePath = "-->"
  private var falsePath = "-->"
}

class Transition<STATE_ID : Enum<STATE_ID>, TRIGGER_ID : Enum<TRIGGER_ID>> {
  val triggerID: TRIGGER_ID
  val name: String
  val source: STATE_ID
  private var target: STATE_ID? = null
  var choice: Choice<STATE_ID>? = null
  var condition: Condition? = null
  var action: Action? = null

  constructor(triggerID: TRIGGER_ID,
              source: STATE_ID,
              target: STATE_ID,
              condition: Condition? = null,
              action: Action? = null) {
    this.triggerID = triggerID
    this.name = this.triggerID.toString()
    this.source = source
    this.target = target
    this.condition = condition
    this.action = action
  }
  constructor(triggerID: TRIGGER_ID,
              source: STATE_ID,
              choice: Choice<STATE_ID>,
              condition: Condition? = null,
              action: Action? = null) {
    this.triggerID = triggerID
    this.name = this.triggerID.toString()
    this.source = source
    this.choice = choice
    this.condition = condition
    this.action = action
  }
  companion object {
    private var counter = 0
    private fun getUniqueChoiceName(): String {
      counter++
      return "choice$counter"
    }
  }

  fun getTarget(): STATE_ID {
    if (choice != null)
    {
      target = choice!!.getTarget()
    }
    if (target == null) throw Exception("Target state not set")
    return target!!
  }

  fun evaluate() : Boolean {
    if (condition != null) {
      return condition?.evaluate()!!
    }
    return true
  }

  fun execute() {
    action?.execute()
  }

  fun puml(actualTrigger : TRIGGER_ID?) : String {
    var choiceState = ""
    var choiceConnections = ""
    val activeTransition = (actualTrigger != null && actualTrigger == triggerID)
    var targetName = target.toString()
    if (choice != null) {
      targetName = getUniqueChoiceName()
      choiceState = "state $targetName <<choice>>\n"
      choiceConnections = choice?.puml(targetName, activeTransition).toString()
    }
    val actionName = if (action != null) "/${action?.name}" else ""
    val conditionName = if (condition != null) "[${condition?.name}]" else ""
    val transitionArrow = if (activeTransition) "-[#Red]->" else "-->"

    return "$choiceState$source $transitionArrow $targetName : $name$conditionName$actionName\n$choiceConnections"
  }
}

interface IStateMachine<STATE_ID : Enum<STATE_ID>, TRIGGER_ID : Enum<TRIGGER_ID>> {
  fun trigger(triggerId : TRIGGER_ID)
  fun start()
  fun stop()
  fun current() : STATE_ID
  val actualState : LiveData<STATE_ID>
  val actualDeepState : LiveData<STATE_ID>
}

class StateMachine<STATE_ID : Enum<STATE_ID>, TRIGGER_ID : Enum<TRIGGER_ID>>(val name: String, val initial: STATE_ID) : IStateMachine<STATE_ID, TRIGGER_ID> {
  override var actualState = MutableLiveData<STATE_ID>()
  override var actualDeepState = MutableLiveData<STATE_ID>()
  private var actualTrigger: TRIGGER_ID? = null
  private var started = false
  internal var subSM: Boolean = false

  init {
    actualState.setValue(initial)
    actualDeepState.setValue(initial)
  }

  fun add(state : State<STATE_ID, TRIGGER_ID>) {
    if (states.containsKey(state.stateID)) {
      throw Exception("Already existing state entry: $state")
    }
    states[state.stateID] = state
  }

  fun add(transition : Transition<STATE_ID, TRIGGER_ID>) {
    if (!states.containsKey(transition.source)) {
      throw Exception("Not existing state entry: $transition.source")
    }
    states[transition.source]?.add(transition)
  }

  override fun trigger(triggerId: TRIGGER_ID) {
    val curState = currentState()
    if (curState.evaluateTransition(triggerId)) {
      actualTrigger = triggerId
      curState.stop()
      curState.executeTransitionAction(triggerId)
      val target = curState.targetState(triggerId)
      if (target != null) {
        actualState.setValue(target)
        val targetState = states[target]
                          ?: throw Exception("State not existing: ${actualState.value}")
        targetState.start()
      }
    } else {
      curState.triggerOnSubStateMachine(triggerId)
    }
    actualDeepState.setValue(currentDeepState())
    if (!subSM) {
      Log.v("SM_LOGGER", "$this")
    }
  }

  override fun start() {
    if (!started) {
      started = true
      actualState.setValue(initial)
      currentState().start()
      actualDeepState.setValue(currentDeepState())
    } else {
      throw Exception("StateMachine already started")
    }
  }

  override fun stop() {
    if (started) {
      started = false
      actualTrigger = null
      currentState().stop()
    } else {
      throw Exception("StateMachine already stopped")
    }
  }

  override fun current(): STATE_ID {
    return actualState.value ?: initial
  }

  fun currentDeepState(): STATE_ID {
    return currentState().currentDeepState()
  }

  override fun toString() : String {
    return pumlHeader() + "\n" + puml() + "\n" + pumlFooter()
  }

  fun pumlHeader() =
    """
      Statemachine plantUML print out:
      @startuml
      scale 350 width
      skinparam backgroundColor White
      skinparam state {
        BackgroundColor<<CURRENT>> LightBlue
        BackgroundColor White
        BorderColor Black
        ArrowColor Black
      }
    """.trimIndent()

  fun pumlFooter() = "@enduml\n\n"

  fun puml() : String {
    var retVal = """
      state $name {
    """.trimIndent()
    for (state in states)
    {
      retVal += "\n" + state.value.puml(actualTrigger)
    }

    var initialTransition  = "-->"
    var currentStateTag = ""
    if (started) {
      currentStateTag = " <<CURRENT>>"
      if (actualTrigger == null){
        initialTransition = "-[#Red]->"
      }
    }

    retVal += "\n"
    retVal += """
        state ${currentState().name}$currentStateTag
        [*] $initialTransition ${states[initial]?.name}
      }
    """.trimIndent()
    return retVal
  }

  private fun currentState() : State<STATE_ID, TRIGGER_ID> {
    return states[actualState.value] ?: throw Exception("Current state not existing: ${actualState.value}")
  }

  private val states= mutableMapOf<STATE_ID, State<STATE_ID, TRIGGER_ID>>()
}
