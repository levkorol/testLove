package com.leokorol.testlove.ui.menu.togetherTests.testsTexts

data class Test(
    val questions: List<QuestionWithVariants>,
    val onlyForSingle: Boolean = false
)

class QuestionWithVariants(val question: String, val answerVariants: Array<String>) {
    var answer: String? = null

}