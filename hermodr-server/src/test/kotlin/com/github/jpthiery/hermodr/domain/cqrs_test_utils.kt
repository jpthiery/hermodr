package com.github.jpthiery.hermodr.domain

/*
    Copyright 2019 Jean-Pascal Thiery

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

import arrow.core.Either
import assertk.assertThat
import assertk.assertions.*
import assertk.fail
import kotlin.reflect.KClass

//  Decide tester
data class DecideFixture<C : Command, S : State, E : Event>(val command: C, val initialState: S, val expectedResult: DecideResultExpected<E>)

sealed class DecideResultExpected<E : Event>

data class FailedDecideResultExpected<E : Event> private constructor(val reason: String?) : DecideResultExpected<E>() {
    companion object {
        fun <E : Event> failedWithoutCheckedReason(): FailedDecideResultExpected<E> = FailedDecideResultExpected(null)
        fun <E : Event> failWithReason(reason: String): FailedDecideResultExpected<E> = FailedDecideResultExpected(reason)
    }
}

data class SuccessDecideResultExpected<E : Event> private constructor(val events: List<KClass<out Event>>) : DecideResultExpected<E>() {
    companion object {
        fun <E : Event> commandSucceeded(events: List<KClass<out Event>>): SuccessDecideResultExpected<E> = SuccessDecideResultExpected(events)
        fun <E : Event> commandSucceeded(event: KClass<out Event>): SuccessDecideResultExpected<E> = SuccessDecideResultExpected(listOf(event))
    }
}

data class NoopDecideResultExpected<E : Event>(val reason: String?) : DecideResultExpected<E>() {
    companion object {
        fun <E : Event> commandNoop(): NoopDecideResultExpected<E> = NoopDecideResultExpected(null)
    }
}

fun <C : Command, S : State, E : Event> assertOnDecideFixture(decideFixture: DecideFixture<C, S, E>, aggregate: Aggregate<C, S, E>): () -> Unit {
    return {
        val result = aggregate.decide(decideFixture.command, decideFixture.initialState)
        when (val expected = decideFixture.expectedResult) {
            is SuccessDecideResultExpected -> {
                when (result) {
                    is Either.Left<String> -> fail("Expecting a success command handle, obtain a failed")
                    is Either.Right<List<Event>> -> {
                        val actualEvents = result.b
                        assertThat(actualEvents).hasSameSizeAs(expected.events)
                        for ((index, expectedEntry) in expected.events.withIndex()) {
                            assertThat(actualEvents.size).isGreaterThan(index)
                            val actualEntry = actualEvents[index]
                            assertThat(actualEntry).isInstanceOf(expectedEntry)
                        }
                    }
                }
            }
            is FailedDecideResultExpected -> {
                when (result) {
                    is Either.Left<String> -> {
                        if (expected.reason != null) {
                            assertThat(result.a).isEqualTo(expected.reason)
                        }
                    }
                    is Either.Right<List<Event>> -> fail("Expecting a failed command handle, obtain a success")
                }
            }
            is NoopDecideResultExpected -> {
                when (result) {
                    is Either.Left<String> -> fail("Expecting handle command no return events, obtain a failed")
                    is Either.Right<List<Event>> -> {
                        if (result.b.isNotEmpty()) {
                            fail("Expecting handle command no return events, obtain a success with ${result.b.size} events")
                        }
                    }
                }
            }
        }
    }
}

interface DecideStateAppender<C : Command, S : State, E : Event> {
    fun given(initialState: S): DecideCommandAppender<C, S, E>
}

interface DecideCommandAppender<C : Command, S : State, E : Event> {
    fun whenApplyCommand(command: C): DecideExpectedAppender<C, S, E>
}

interface DecideExpectedAppender<C : Command, S : State, E : Event> {
    fun then(decideExpected: DecideResultExpected<E>): DecideFixture<C, S, E>
}

// https://youtrack.jetbrains.com/issue/KT-7770
fun <C : Command, S : State, E : Event> decideTestWith(): DecideStateAppender<C, S, E> {
    return object : DecideStateAppender<C, S, E> {
        override fun given(initialState: S): DecideCommandAppender<C, S, E> {
            return object : DecideCommandAppender<C, S, E> {
                override fun whenApplyCommand(command: C): DecideExpectedAppender<C, S, E> {
                    return object : DecideExpectedAppender<C, S, E> {
                        override fun then(decideExpected: DecideResultExpected<E>): DecideFixture<C, S, E> {
                            return DecideFixture(command, initialState, decideExpected)
                        }
                    }
                }
            }
        }
    }
}

//  Apply tester
data class ApplierFixture<S : State, E : Event>(val initialState: S, val eventToApply: E, val expectedState: S)

fun <C : Command, S : State, E : Event> assertOnApply(fixture: ApplierFixture<S, E>, aggregate: Aggregate<C, S, E>): () -> Unit {
    return {
        val result = aggregate.apply(fixture.initialState, fixture.eventToApply)
        if (fixture.expectedState::class.isData) {
            assertk.assertThat(result::class.isData).isTrue()
            assertk.assertThat(result).isDataClassEqualTo(fixture.expectedState)
        } else {
            assertk.assertThat(result).isEqualTo(fixture.expectedState)
        }
    }
}

interface ApplierStateAppender<S : State, E : Event> {
    fun given(initialState: S): ApplierEventAppender<S, E>
}

interface ApplierEventAppender<S : State, E : Event> {
    fun whenApplyEvent(event: E): ApplierExpectedAppender<S, E>
}

interface ApplierExpectedAppender<S : State, E : Event> {
    fun then(expectedState: S): ApplierFixture<S, E>
}

fun <S : State, E : Event> applierTestWith(): ApplierStateAppender<S, E> {
    return object : ApplierStateAppender<S, E> {
        override fun given(initialState: S): ApplierEventAppender<S, E> {
            return object : ApplierEventAppender<S, E> {
                override fun whenApplyEvent(eventToApply: E): ApplierExpectedAppender<S, E> {
                    return object : ApplierExpectedAppender<S, E> {
                        override fun then(expectedState: S): ApplierFixture<S, E> {
                            return ApplierFixture(initialState, eventToApply, expectedState)
                        }
                    }
                }
            }
        }
    }
}