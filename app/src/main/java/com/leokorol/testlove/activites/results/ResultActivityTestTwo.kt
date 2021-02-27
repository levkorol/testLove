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

class ResultActivityTestTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_test_two)


        AuthManager2.setTest2Listener { my, partner ->
            showToast("Вы оба пришли тест 2. Можете посмотреть результаты в результатах")
        }

        goTestActivity.setOnClickListener { replaceActivity(MenuTestsActivity()) }

        Results.getResultsPart2(0) //todo вписать сколько получили совпадающих ответов с подключенным партнером
    }
}