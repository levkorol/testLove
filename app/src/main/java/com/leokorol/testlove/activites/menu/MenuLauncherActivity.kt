package com.leokorol.testlove.activites.menu

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.menu.connect.ConnectActivity
import com.leokorol.testlove.activites.menu.intresting.InterestingActivity
import com.leokorol.testlove.activites.menu.togetherTests.TogetherEnterNameActivity
import com.leokorol.testlove.data_base.AuthManager2
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_menu.*

class MenuLauncherActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        this.setContentView(R.layout.activity_menu)

        initClick()
        //  whatMyName()
        setupDisconnectButton()

        AuthManager2.isConnectedPartner(TestApp.getUserCode(), {
            visiblePartner(true)
        }, {
            visiblePartner(false)
        })
    }


    private fun setupDisconnectButton() {    //todo отсоеденить партнера и удалить все сессии в тестах

        val refMyConnect = database.getReference(TestApp.getUserCode()).child("partner")
        refMyConnect.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    visiblePartner(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        btnDisconnect.setOnClickListener {
            refMyConnect.removeValue()
            val refPartnerConnect = database.getReference(TestApp.getPartnerCode()).child("partner")
            refPartnerConnect.removeValue()
            visiblePartner(false)
        }
    }

//    private fun whatMyName() {  //todo сохранить имя свое в базу и что бы оно передавалось партнеру при подключении
//        val myNameEditText = findViewById<EditText>(R.id.name_user)
//        myNameEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable?) {
//                TestApp.sharedPref?.edit()?.putString(TestApp.USER_NAME, s.toString())?.apply()
//            }
//        })
//    }

    private fun goToTestsActivity() {
        AuthManager2.isConnectedPartner(TestApp.getUserCode(), {
            replaceActivity(MenuTestsActivity())
        }, {
            showToast("Для прохождения теста необходимо соединиться с партнёром")
        })
    }

    private fun goConnectActivity() {
        val cm = (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager)
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            //  AuthManager.instance.subscribeToSessions()
            replaceActivity(ConnectActivity())
        } else {
            showToast("Нет подключения к интернету")
        }
    }

    private fun visiblePartner(isVisible: Boolean) {
        if (isVisible) {
            partner.visibility = View.VISIBLE
        } else {
            partner.visibility = View.GONE
        }
    }

    private fun initClick() {
        btnGoConnectActivity.setOnClickListener { goConnectActivity() }
        btnGoMenuTestsActivity.setOnClickListener { goToTestsActivity() }
        btnGoTogetherTestsActivity.setOnClickListener { replaceActivity(TogetherEnterNameActivity()) }
        goIntresting.setOnClickListener { replaceActivity(InterestingActivity()) }
        edit_my_info.setOnClickListener { dialog() }
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun dialog() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.DialogStyle)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_name_layout, null)
        dialogBuilder.setView(dialogView)
//        val radioGroupChat = dialogView.radio_group_chat
//        dialogView.radioButton_user_chat.isChecked = true
        dialogBuilder.setPositiveButton("Сохранить") { dialog, id ->
//            when (radioGroupChat.checkedRadioButtonId) {
//                R.id.radioButton_user_chat -> {
//                    (activity as HomeActivity).replaceFragment(
//                        MySkippersFragment.getInstance(
//                            isFromChat = true
//                        )
//                    )
//                }
//                R.id.radioButton_circle_chat -> {
//                    (activity as HomeActivity).replaceFragment(
//                        PickCircleFragment.getInstance(
//                            PickCircleFragment.NEW_CIRCLE_CHAT
//                        ), true
//                    )
//                }
            // }
        }
        dialogBuilder.setNegativeButton("Отмена") { _, _ -> }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}