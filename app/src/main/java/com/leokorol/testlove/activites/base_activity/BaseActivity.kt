package com.leokorol.testlove.activites.base_activity

import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.data_base.AuthManager2
import com.leokorol.testlove.utils.showToast

class BaseActivity : AppCompatActivity() {

    private fun subscribeToResults() {
        // TODO выбрать несложную активность
        // TODO реализовать отображение РЕЗУЛЬТАТЫ ПРОЙДЕНЫ
        // TODO потом вынести этот метод куда-нибудь (например в базовую активность)
        AuthManager2.setTest1Listener { my, partner ->
            showToast("Вы оба пришли тест 1. Можете посмотреть результаты в результатах")
        }
    }
}