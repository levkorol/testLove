package com.leokorol.testlove.ui.menu.intresting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.ui.menu.MenuLauncherActivity
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_interesting.*

class InterestingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interesting)

        clickListeners()
    }

    private fun clickListeners() {
        igoMenuActivity.setOnClickListener { replaceActivity(MenuLauncherActivity()) }

        buttonGoSeeTodo.setOnClickListener {
            intentBrowser("https://play.google.com/store/apps/details?id=com.levkorol.todo")
        }

        goTestOnPlayMarket.setOnClickListener {
            intentBrowser("https://play.google.com/store/apps/details?id=com.leokorol.testlove")
        }
    }

    private fun intentBrowser(string: String) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(string)
        )
        startActivity(browserIntent)
    }
}