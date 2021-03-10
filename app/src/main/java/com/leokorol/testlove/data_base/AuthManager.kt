package com.leokorol.testlove.data_base

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.ui.base.ISimpleListener

class AuthManager {

    var isInQueue = false
        private set
    var isConnectedToPartner = false
        private set
    private var _currentPart = 0

    var currentPart: Int
        get() = _currentPart
        set(currentPart) {
            _currentPart = currentPart
            TestApp.sharedPref?.edit()
                ?.putInt(TestApp.LAST_PART, _currentPart)?.apply()
        }


    fun tryMoveToSession(
        partnerCode: String,
        successListener: ISimpleListener?,
        failListener: ISimpleListener?
    ) {
        val database = FirebaseDatabase.getInstance()
        val queueRef = database.getReference("queue")

        isConnectedToPartner = false
        isInQueue = true
        queueRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return
                }
                var found = false

                for (snapshot in dataSnapshot.children) {
                    val code = snapshot.value as String?
                    if (partnerCode.equals(code, ignoreCase = true)) {
                        found = true
                        break
                    }
                }
                if (found) {
                    successListener?.eventOccured()
                } else {
                    failListener?.eventOccured()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    val deviceId: String
        get() = TestApp.sharedPref?.getString(TestApp.DEVICE_ID, "") ?: ""
    val code: String
        get() = TestApp.sharedPref?.getString(TestApp.MY_CODE, "") ?: ""

    companion object {
        val instance = AuthManager()
    }
}