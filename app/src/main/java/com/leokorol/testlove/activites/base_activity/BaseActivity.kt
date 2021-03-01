package com.leokorol.testlove.activites.base_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.data_base.AuthManager2
import com.leokorol.testlove.utils.showToast

open class BaseActivity : AppCompatActivity() {

    open val isTestListenersEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isTestListenersEnabled) enableTestListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isTestListenersEnabled) enableTestListeners()
    }

    protected open fun onResultsDone(count: Int) {}

    private fun enableTestListeners() {
        // подписываем AuthManager2 на результаты
        AuthManager2.isConnectedPartner(TestApp.getUserCode(), {
            AuthManager2.subscribePartnerTestResults(TestApp.getPartnerCode())
            AuthManager2.subscribeMyTestResults(TestApp.getUserCode())
        }, {})

        AuthManager2.setTest1Listener { my, partner ->
            showToast("Вы оба пришли тест 1. Можете посмотреть результаты ")
        }

        AuthManager2.setTest2Listener { my, partner ->
            showToast("Вы оба пришли тест 2. Можете посмотреть результаты ")
        }

        AuthManager2.setTest3Listener { my, partner ->
            // TODO 0 отдельный метод параметры: номерТеста, my, partner
            // TODO 1 нужно проверить my, partner != null & my.size, partner.size return
            // TODO 2 делаем цикл по вопросам for (i in 0 until количество вопросов) { теория: kotlin ranges
            // TODO 3 проверяем совпадают ли ответы my!![i] == partner!![i] + переменная
            // TODO 4 onResultsDone(число совпадений)
            showToast("Вы оба пришли тест 3. Можете посмотреть результаты ")
        }
    }

    private fun disableTestListeners() {
        AuthManager2.setTest1Listener { _, _ -> }
        AuthManager2.setTest2Listener { _, _ -> }
        AuthManager2.setTest3Listener { _, _ -> }
    }
}