package com.leokorol.testlove.ui.menu.togetherTests.results

import android.os.Bundle
import com.leokorol.testlove.R
import com.leokorol.testlove.ui.base.BaseActivity
import com.leokorol.testlove.ui.menu.togetherTests.MenuTestsActivity
import com.leokorol.testlove.ui.menu.togetherTests.testsTexts.Results
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_result_test_three.*

class ResultActivityTestThree : BaseActivity() {

    override val isTestListenersEnabled: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_test_three)

        goTestActivity.setOnClickListener { replaceActivity(MenuTestsActivity()) }
    }

    override fun onResultsDone(count: Int) {
        super.onResultsDone(count)
        resultsThreeTestTextView.text = Results.getResultsPart3(count)
    }

}