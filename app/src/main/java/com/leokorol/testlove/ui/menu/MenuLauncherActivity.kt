package com.leokorol.testlove.ui.menu

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.data_base.AuthManagerTest
import com.leokorol.testlove.ui.menu.connect.ConnectActivity
import com.leokorol.testlove.ui.menu.connect.SuccessConnectActivity
import com.leokorol.testlove.ui.menu.intresting.InterestingActivity
import com.leokorol.testlove.ui.menu.singleTests.SingleTestsActivity
import com.leokorol.testlove.ui.menu.togetherTests.MenuTestsActivity
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.dialog_name_layout.view.*


class MenuLauncherActivity : AppCompatActivity() {

    private lateinit var mAdView: AdView
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference(TestApp.getUserCode())
    private val partnerRef = database.getReference(TestApp.getPartnerCode())
    private var gender = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        this.setContentView(R.layout.activity_menu)

        myRef.child("partnerName")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        partner_name.text = snapshot.value.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        initAdsGoogle()
        initPrefs()
        initClick()

        AuthManagerTest.isConnectedPartner(TestApp.getUserCode(), {
            visiblePartner(true)
        }, {
            visiblePartner(false)
        })
    }

    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

    private fun initAdsGoogle() {
        MobileAds.initialize(this) {}
        val listId = listOf("AC15A1685814DB24010455FD90B6BAC9")
        val configuration = RequestConfiguration
            .Builder()
            .setTestDeviceIds(listId)
            .build()
        MobileAds.setRequestConfiguration(configuration)

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder()
            .build()

        mAdView.loadAd(adRequest)

    }

    private fun initPrefs() {
        if (TestApp.getUserName().isNotBlank()) {
            name_user_.text = TestApp.getUserName()
        }

        if (TestApp.getUserGender() > 0) {
            TestApp.getUserGender()
            updateUserGenderIcon(gender)
        }
    }

    private fun initClick() {

        btnDisconnect.setOnClickListener {
            partnerRef.removeValue()
            myRef.removeValue()

            TestApp.sharedPref?.edit()?.putInt(TestApp.LAST_QUESTION_1, 0)?.apply()
            TestApp.sharedPref?.edit()?.putInt(TestApp.LAST_QUESTION_2, 0)?.apply()
            TestApp.sharedPref?.edit()?.putInt(TestApp.LAST_QUESTION_3, 0)?.apply()
            //TestApp.savePartnerName("Партнер")
            visiblePartner(false)
        }

        btnGoConnectActivity.setOnClickListener {
            AuthManagerTest.isConnectedPartner(TestApp.getUserCode(), {
                replaceActivity(SuccessConnectActivity())
            }, {
                replaceActivity(ConnectActivity())
            })
        }

        btnGoMenuTestsActivity.setOnClickListener { replaceActivity(SingleTestsActivity()) }
        btnGoTogetherTestsActivity.setOnClickListener { goToTestsActivity() }
        goIntresting.setOnClickListener { replaceActivity(InterestingActivity()) }
        edit_my_info.setOnClickListener { showDialog() }
    }


    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.DialogStyle)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_name_layout, null)
        dialogBuilder.setView(dialogView)
        //    val radioGroup = dialogView.radio_group
//        radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.radio_woman -> {
//                    gender = 1
//                }
//                R.id.radio_man -> {
//                    gender = 2
//                }
//            }
//        }

        dialogBuilder.setPositiveButton("Сохранить") { _, _ ->

            val queueRef = database.getReference("names").child(TestApp.getUserCode())

            if (dialogView.edit_name.text.isNotBlank()) {
                TestApp.saveUserName(dialogView.edit_name.text.toString())
                queueRef.setValue(dialogView.edit_name.text.toString())
            }
            if (TestApp.getUserName().isNotBlank()) {
                name_user_.text = TestApp.getUserName()
            }

            if (gender == 1) TestApp.saveUserGender(1)
            if (gender == 2) TestApp.saveUserGender(2)

            if (TestApp.getUserGender() > 0) {

                TestApp.getUserGender()
                updateUserGenderIcon(gender)
            }
        }

        dialogBuilder.setNegativeButton("Отмена") { _, _ -> }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun updateUserGenderIcon(gender: Int) {
        when (gender) {
            1 -> {
                gender_user.setImageResource(R.drawable.ic_woman)
            }
            2 -> {
                gender_user.setImageResource(R.drawable.ic_man)
            }
        }
    }

    private fun goToTestsActivity() {
        AuthManagerTest.isConnectedPartner(TestApp.getUserCode(), {
            replaceActivity(MenuTestsActivity())
        }, {
            showToast("Для прохождения теста необходимо соединиться с партнёром")
        })
    }

    private fun visiblePartner(isVisible: Boolean) {
        if (isVisible) {
            partner.visibility = View.VISIBLE
        } else {
            partner.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

}
