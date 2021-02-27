package com.leokorol.testlove.activites.results

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.activites.menu.MenuTestsActivity
import com.leokorol.testlove.data_base.AuthManager2
import com.leokorol.testlove.tests.texts.Results
import com.leokorol.testlove.utils.replaceActivity
import com.leokorol.testlove.utils.showToast
import kotlinx.android.synthetic.main.activity_result_test_three.*

class ResultsActivityTestOne : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        AuthManager2.setTest1Listener { my, partner ->
            showToast("Вы оба пришли тест 1. Можете посмотреть результаты в результатах")
        }

        goTestActivity.setOnClickListener { replaceActivity(MenuTestsActivity()) }

        Results.getResultsPart1(0) //todo вписать сколько получили совпадающих ответов с подключенным партнером

    }
}