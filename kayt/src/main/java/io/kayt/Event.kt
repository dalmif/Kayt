package io.kayt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

interface UiEvent

fun interface EventHandler<in UE : UiEvent> {
    fun onEvent (event : UE)
}

inline infix operator fun <UE : UiEvent, reified CUE : UE> EventHandler<UE>.plus (rh : EventHandler<CUE>)  : EventHandler<UE>{
    return EventHandler {
        this.onEvent(it)
        if (it is CUE) {
            rh.onEvent(it)
        }
    }
}

open class Event<UE: UiEvent>(private val eventHandler : EventHandler<UE>) {
    fun on (event : UE) {
        eventHandler.onEvent(event)
    }

    fun <CHILD : UE> scoped(onEvent : EventHandler<CHILD> = eventHandler) : Event<CHILD> {
        return Event<CHILD>(onEvent)
    }

    fun ref(event : UE) : kotlin.reflect.KFunction0<Unit> {
        return {on(event) }::invoke
    }

    @JvmName("ref0Param")
    fun <U: UE> ref(invokable: kotlin.reflect.KFunction0<U>) : kotlin.reflect.KFunction0<Unit> {
        return {on(invokable()) }::invoke
    }

    @JvmName("ref1Params")
    fun <P1, U: UE> ref(invokable: kotlin.reflect.KFunction1<P1, U>) : kotlin.reflect.KFunction1<P1, Unit> {
        return {value : P1 -> on(invokable(value)) }::invoke
    }

    fun <P1, U: UE> ref(invokable: kotlin.reflect.KFunction1<P1, U>, param : P1) : kotlin.reflect.KFunction0<Unit> {
        return { on(invokable(param)) }::invoke
    }

    @JvmName("ref2Params")
    fun <P1,P2, U: UE> ref(invokable: kotlin.reflect.KFunction2<P1,P2, U>) : kotlin.reflect.KFunction2<P1,P2, Unit> {
        return {value : P1, value2 : P2 -> on(invokable(value,value2)) }::invoke
    }

    fun <P1,P2, U: UE> ref(invokable: kotlin.reflect.KFunction2<P1,P2, U>, param1 : P1, param2 : P2) : kotlin.reflect.KFunction0<Unit> {
        return { on(invokable(param1,param2)) }::invoke
    }

    @JvmName("ref3Params")
    fun <P1,P2,P3, U: UE> ref(invokable: kotlin.reflect.KFunction3<P1,P2,P3, U>) : kotlin.reflect.KFunction3<P1,P2,P3, Unit> {
        return {v1 : P1, v2 : P2, v3 : P3 -> on(invokable(v1,v2,v3)) }::invoke
    }

    fun <P1,P2,P3, U: UE> ref(invokable: kotlin.reflect.KFunction3<P1,P2,P3, U>, param1 : P1, param2 : P2, param3 : P3) : kotlin.reflect.KFunction0<Unit> {
        return { on(invokable(param1,param2,param3)) }::invoke
    }

    @JvmName("ref4Params")
    fun <P1,P2,P3,P4, U: UE> ref(invokable: kotlin.reflect.KFunction4<P1,P2,P3,P4, U>) : kotlin.reflect.KFunction4<P1,P2,P3,P4, Unit> {
        return {v1 : P1, v2 : P2, v3 : P3 ,v4 : P4 -> on(invokable(v1,v2,v3, v4)) }::invoke
    }

    fun <P1,P2,P3,P4, U: UE> ref(invokable: kotlin.reflect.KFunction4<P1,P2,P3,P4, U>, param1 : P1, param2 : P2, param3 : P3, param4: P4) : kotlin.reflect.KFunction0<Unit> {
        return { on(invokable(param1,param2,param3, param4)) }::invoke
    }

    @JvmName("ref5Params")
    fun <P1,P2,P3,P4,P5, U: UE> ref(invokable: kotlin.reflect.KFunction5<P1,P2,P3,P4,P5, U>) : kotlin.reflect.KFunction5<P1,P2,P3,P4,P5, Unit> {
        return {v1 : P1, v2 : P2, v3 : P3 ,v4 : P4, v5: P5 -> on(invokable(v1,v2,v3, v4,v5)) }::invoke
    }

    fun <P1,P2,P3,P4,P5, U: UE> ref(invokable: kotlin.reflect.KFunction5<P1,P2,P3,P4,P5, U>, param1 : P1, param2 : P2, param3 : P3, param4: P4, param5 : P5) : kotlin.reflect.KFunction0<Unit> {
        return { on(invokable(param1,param2,param3, param4, param5)) }::invoke
    }

    @JvmName("ref6Params")
    fun <P1,P2,P3,P4,P5,P6, U: UE> ref(invokable: kotlin.reflect.KFunction6<P1,P2,P3,P4,P5,P6, U>) : kotlin.reflect.KFunction6<P1,P2,P3,P4,P5,P6, Unit> {
        return {v1 : P1, v2 : P2, v3 : P3 ,v4 : P4, v5: P5, v6 : P6 -> on(invokable(v1,v2,v3, v4,v5, v6)) }::invoke
    }

    fun <P1,P2,P3,P4,P5,P6, U: UE> ref(invokable: kotlin.reflect.KFunction6<P1,P2,P3,P4,P5,P6, U>, param1 : P1, param2 : P2, param3 : P3, param4: P4, param5 : P5, param6 : P6) : kotlin.reflect.KFunction0<Unit> {
        return { on(invokable(param1,param2,param3, param4, param5, param6)) }::invoke
    }

    fun <CUE : UE> plusOp (rh : Event<CUE>, clazz: Class<CUE>) : Event<UE>{
        val lHandler = eventHandler
        val rHandler = rh.eventHandler
        val newEventHandler = Event(EventHandler<UE> {
            lHandler.onEvent(it)
            if (it::class.java.isAssignableFrom(clazz)) {
                rHandler.onEvent(it as CUE)
            }
        })
        return newEventHandler
    }

    inline infix operator fun <reified CUE : UE> plus (rh : Event<CUE>) : Event<UE> {
        return this.plusOp(rh, CUE::class.java)
    }

    @Composable
    inline infix fun <reified CUE : UE> then (rh : Event<CUE>) : Event<UE> {
        val event = remember { this.plusOp(rh, CUE::class.java) }
        return event
    }
}


@Composable
fun <CHILD : UE, UE : UiEvent> EventScope (parent : Event<UE>, scope : @Composable Event<CHILD>.() -> Unit) {
    scope(parent.scoped<CHILD>())
}

@Composable
fun <UE : UiEvent> rememberEvent (eventHandler : EventHandler<UE>) : Event<UE> {
    val event = remember { Event(eventHandler) }
    return event
}