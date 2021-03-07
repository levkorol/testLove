package com.leokorol.testlove.ui.menu.togetherTests.testsBase

import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.ui.base.BaseTestQuestionsActivity
import com.leokorol.testlove.ui.menu.togetherTests.testsTexts.Questions

class TestOneQuestions : BaseTestQuestionsActivity(
    Questions.Part1,
    R.layout.activity_test_one,
    R.drawable.onetitlebg,
    ANSWERS,
    1,
    TestApp.LAST_QUESTION_1
)