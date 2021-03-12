package com.leokorol.testlove.ui.menu.singleTests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.ui.menu.MenuLauncherActivity
import com.leokorol.testlove.utils.replaceActivity
import com.leokorol.testlove.utils.showToast
import kotlinx.android.synthetic.main.activity_together_enter_name.*

class SingleTestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_together_enter_name)

        clickListeners()
    }

    private fun clickListeners() {
        buttonOk.setOnClickListener { showToast("Спасибо! Совсем скоро :)") }
        tgoMenuActivity.setOnClickListener { replaceActivity(MenuLauncherActivity()) }
    }
}