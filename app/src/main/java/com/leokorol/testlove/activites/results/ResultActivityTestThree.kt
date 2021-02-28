package com.leokorol.testlove.activites.results

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.R
import com.leokorol.testlove.activites.menu.MenuTestsActivity
import com.leokorol.testlove.data_base.AuthManager2
import com.leokorol.testlove.tests.texts.Results
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_result_test_three.*

class ResultActivityTestThree : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_test_three)

        AuthManager2.setTest3Listener { my, partner ->

        }

        //   resultsThreeTestTextView.text =
        Results.getResultsPart3(0) //todo вписать сколько получили совпадающих ответов с подключенным партнером

        goTestActivity.setOnClickListener { replaceActivity(MenuTestsActivity()) }
    }

    private fun setResult() {
        val database = FirebaseDatabase.getInstance()
        database.reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //                  val database = FirebaseDatabase.getInstance()
//                    val sessionsRef = database.getReference("sessions")

                // TODO забирать данные по ответам тут

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}