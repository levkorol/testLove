package com.leokorol.testlove.ui.menu.togetherTests.testsBase

import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.ui.base.BaseTestQuestionsActivity
import com.leokorol.testlove.ui.menu.togetherTests.testsTexts.Questions

class TestTwoQuestions : BaseTestQuestionsActivity(
    Questions.Part2,
    R.layout.activity_test_two_questions,
    R.drawable.twotitlebg,
    ANSWERS_2,
    3,
    TestApp.LAST_QUESTION_2
)