package com.github.jpthiery.hermodr.domain

internal class SharedRadioTest {

    /*
    @TestFactory
    fun `Decide on SharedRadio`() = listOf(
            decideTestOnSharedRadiotWith()
                    .given(SharedRadioNotExit)
                    .whenApplyCommand(CreateSharedRadio(sharedRadioId, defaultSharedRadioName, ))
                    .then(commandSucceeded(SharedRadioCreated::class))
    ).map {
        DynamicTest.dynamicTest(
                "When ${it.command::class.simpleName} on shared radio state ${it.initialState::class.simpleName} then expecting ${it.expectedResult}",
                assertOnDecideFixture(it, SharedRadio())
        )
    }
    */
}


//  Alias
fun decideTestOnSharedRadiotWith(): DecideStateAppender<SharedRadioCommand, SharedRadioState, SharedRadioEvent> = decideTestWith()

const val defaultSharedRadioName = "Awersome Hermodr"
val sharedRadioId = SharedRadioId(defaultSharedRadioName)