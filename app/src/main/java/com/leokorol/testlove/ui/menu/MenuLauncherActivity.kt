package com.leokorol.testlove.ui.menu

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

    private val database = FirebaseDatabase.getInstance()
    private val refMyConnect = database.getReference(TestApp.getUserCode()).child("partner")
    private val refPartnerConnect = database.getReference(TestApp.getPartnerCode()).child("partner")
//    private val refMyConnect = database.getReference(TestApp.getUserCode())
//    private val refPartnerConnect = database.getReference(TestApp.getPartnerCode())

    private var gender = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        this.setContentView(R.layout.activity_menu)

        if (TestApp.getUserName().isNotBlank()) {
            name_user_.text = TestApp.getUserName()
        }

        if (TestApp.getUserGender() > 0) {
            TestApp.getUserGender()
            updateUserGenderIcon(gender)
        }

        initClick()

        AuthManagerTest.isConnectedPartner(TestApp.getUserCode(), {
            visiblePartner(true)
        }, {
            visiblePartner(false)
        })
    }

    private fun disconnectPartner(isDisconnect: Boolean) {

        if (isDisconnect) {
            refMyConnect.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists() || isDisconnect) {
                        refMyConnect.removeValue()
                        refPartnerConnect.removeValue()
                        visiblePartner(false)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            refPartnerConnect.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists() || isDisconnect) {
                        refMyConnect.removeValue()
                        refPartnerConnect.removeValue()
                        visiblePartner(false)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    }

    private fun initClick() {

        btnDisconnect.setOnClickListener {

            disconnectPartner(true)

            refPartnerConnect.removeValue()
            refMyConnect.removeValue()

            visiblePartner(false)
        }

        btnGoConnectActivity.setOnClickListener {
            AuthManagerTest.isConnectedPartner(TestApp.getUserCode(), {
                replaceActivity(SuccessConnectActivity())
            }, {
                replaceActivity(ConnectActivity())
            })
        }

        btnGoMenuTestsActivity.setOnClickListener { goToTestsActivity() }
        btnGoTogetherTestsActivity.setOnClickListener { replaceActivity(SingleTestsActivity()) }
        goIntresting.setOnClickListener { replaceActivity(InterestingActivity()) }
        edit_my_info.setOnClickListener { dialog() }
    }


    private fun dialog() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.DialogStyle)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_name_layout, null)
        dialogBuilder.setView(dialogView)
        val radioGroup = dialogView.radio_group


        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_woman -> {
                gender = 1
            }
            R.id.radio_man -> {
                gender = 2
            }
        }
//        if (radio_woman.isChecked) {
//            gender = 1
//        }
//
//        if (radio_man.isChecked) {
//            gender = 2
//        }

        dialogBuilder.setPositiveButton("Сохранить") { _, _ ->

            TestApp.saveUserName(dialogView.edit_name.text.toString())
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