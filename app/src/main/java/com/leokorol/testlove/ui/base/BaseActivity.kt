package com.leokorol.testlove.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.data_base.AuthManagerTest

open class BaseActivity : AppCompatActivity() {

    open val isTestListenersEnabled = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isTestListenersEnabled) {
            enableTestListeners()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isTestListenersEnabled) enableTestListeners()
    }

    protected open fun onResultsDone(count: Int) {}

    private fun enableTestListeners() {
        // подписываем AuthManagerTest на результаты
        AuthManagerTest.isConnectedPartner(TestApp.getUserCode(), {
            AuthManagerTest.subscribePartnerTestResults(TestApp.getPartnerCode())
            AuthManagerTest.subscribeMyTestResults(TestApp.getUserCode())
        }, {})

        AuthManagerTest.setTest1Listener { my, partner ->
            onResultsDone(equalAnswerCount(my, partner))
        }

        AuthManagerTest.setTest2Listener { my, partner ->
            onResultsDone(equalAnswerCount(my, partner))
        }

        AuthManagerTest.setTest3Listener { my, partner ->
            onResultsDone(equalAnswerCount(my, partner))
        }
    }

    private fun equalAnswerCount(
        my: List<List<Any>>?,
        partner: List<List<Any>>?
    ): Int {
        var equalAnswer = -1
        if (my != null && partner != null && my.size == partner.size) {
            for (i in my.indices) {
                if (my[i] == partner[i]) {
                    equalAnswer++
                }
            }
        }
        return equalAnswer
    }

    private fun disableTestListeners() {
        AuthManagerTest.setTest1Listener { _, _ -> }
        AuthManagerTest.setTest2Listener { _, _ -> }
        AuthManagerTest.setTest3Listener { _, _ -> }
    }
}


//            if (my != null && partner != null && my.size == partner.size) {
//                for (i in my.indices) {
//                  for (j in my[i].indices) {
//                        if(my[i][j] == partner[i][j]) {
//                            sameAnswerTest3++
//                        }
//               }
//  }