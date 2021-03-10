package com.leokorol.testlove.data_base

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.TestApp

object AuthManagerTest {

    private var isInitialised = false
    private lateinit var partnerConnectedListener: () -> Unit

    private lateinit var test1Listener: (myResults: List<List<Any>>?, partnerResults: List<List<Any>>?) -> Unit
    private lateinit var test2Listener: (myResults: List<List<Any>>?, partnerResults: List<List<Any>>?) -> Unit
    private lateinit var test3Listener: (myResults: List<List<Any>>?, partnerResults: List<List<Any>>?) -> Unit

    private var test1PartnerResults: List<List<Any>>? = null
    private var test2PartnerResults: List<List<Any>>? = null
    private var test3PartnerResults: List<List<Any>>? = null

    private var test1MyResults: List<List<Any>>? = null
    private var test2MyResults: List<List<Any>>? = null
    private var test3MyResults: List<List<Any>>? = null

    private val database = FirebaseDatabase.getInstance()

    fun connectToPartner(partnerCode: String) {
        var partnerNameValue = "Партнер"
        //    TestApp.savePartnerName(partnerNameValue)

        val namesRef = database.getReference("names")
        namesRef.addListenerForSingleValueEvent(AppValueEventListener {
            for (snapshot in it.children) {
                if (it.hasChild(partnerCode)) {
                    partnerNameValue = it.child(partnerCode).value.toString()
                    TestApp.savePartnerName(partnerNameValue)
                }
            }
        })

        val myCode = TestApp.sharedPref?.getString(TestApp.MY_CODE, "")

        myCode?.let { database.getReference(it).child("partner") }?.setValue(partnerCode)
        myCode?.let { database.getReference(it).child("partnerName") }
            ?.setValue(TestApp.getPartnerName())

        val partnerRef = database.getReference(partnerCode)

        partnerCode.let { partnerRef.child("partner") }.setValue(myCode)
        partnerCode.let { partnerRef.child("partnerName") }.setValue(TestApp.getUserName())


        test1PartnerResults = null
        test2PartnerResults = null
        test3PartnerResults = null

        subscribePartnerTestResults(partnerCode)

    }

    fun saveAnswer(testNumber: Int, questionNumber: Int, answerNumbers: Set<String>) {
        TestApp.sharedPref?.edit()
            ?.putStringSet("answer_${testNumber}_${questionNumber}", answerNumbers)?.apply()
    }

    fun copyAnswersFromPrefsToDatabase(testNumber: Int, questionsCount: Int) {
        val answersRef = database.getReference(TestApp.getUserCode()).child("test_${testNumber}")
        for (i in 0 until questionsCount) {
            val answers = TestApp.sharedPref?.getStringSet("answer_${testNumber}_${i}", null)
            if (answers != null) {
                answersRef.child(i.toString()).setValue(answers.map { it.toInt() })
            }
        }
    }

    fun isConnectedPartner(
        myCode: String,
        connectedToPartner: () -> Unit,
        notConnectedToPartner: () -> Unit
    ) {

        val sessionsRef = database.getReference(myCode).child("partner")
        sessionsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    notConnectedToPartner()
                    return
                }
                if (dataSnapshot.value != "") {
                    connectedToPartner()
                } else {
                    notConnectedToPartner()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun subscribePartnerTestResults(partnerCode: String) {

        val test1RefPartner = database.getReference(partnerCode).child("test_0")
        val test2RefPartner = database.getReference(partnerCode).child("test_1")
        val test3RefPartner = database.getReference(partnerCode).child("test_2")

        test1RefPartner.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                test1PartnerResults = dataSnapshot.value as List<List<Any>>?
                checkAnswers()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        test2RefPartner.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                test2PartnerResults = dataSnapshot.value as List<List<Any>>?
                checkAnswers()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        test3RefPartner.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                test3PartnerResults = dataSnapshot.value as List<List<Any>>?
                checkAnswers()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun subscribeMyTestResults(myCode: String) {

        val test1RefMy = database.getReference(myCode).child("test_0")
        val test2RefMy = database.getReference(myCode).child("test_1")
        val test3RefMy = database.getReference(myCode).child("test_2")

        test1RefMy.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                test1MyResults = dataSnapshot.value as List<List<Any>>?
                checkAnswers()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        test2RefMy.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                test2MyResults = dataSnapshot.value as List<List<Any>>?
                checkAnswers()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        test3RefMy.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                test3MyResults = dataSnapshot.value as List<List<Any>>?
                checkAnswers()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun checkAnswers() {
        if (test1MyResults != null && test1PartnerResults != null) {
            test1Listener(test1MyResults, test1PartnerResults)
        }
        if (test2MyResults != null && test2PartnerResults != null) {
            test2Listener(test2MyResults, test2PartnerResults)
        }
        if (test3MyResults != null && test3PartnerResults != null) {
            test3Listener(test3MyResults, test3PartnerResults)
        }
    }

    fun setTest1Listener(value: (myResults: List<List<Any>>?, partnerResults: List<List<Any>>?) -> Unit) {
        test1Listener = value
    }

    fun setTest2Listener(value: (myResults: List<List<Any>>?, partnerResults: List<List<Any>>?) -> Unit) {
        test2Listener = value
    }

    fun setTest3Listener(value: (myResults: List<List<Any>>?, partnerResults: List<List<Any>>?) -> Unit) {
        test3Listener = value
    }

    fun initPartnerConnectedListener(myCode: String) {
        val database = FirebaseDatabase.getInstance()
        val sessionsRef = database.getReference(myCode).child("partner")
        sessionsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.value != "" && isInitialised) {
                    partnerConnectedListener()

                    test1PartnerResults = null
                    test2PartnerResults = null
                    test3PartnerResults = null
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setPartnerConnectedListener(listener: () -> Unit) {
        partnerConnectedListener = listener
        isInitialised = true
    }
}


