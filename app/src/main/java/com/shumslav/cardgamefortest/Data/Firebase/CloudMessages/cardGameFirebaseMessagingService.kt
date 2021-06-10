package com.sigufyndufi.finfangam.Data.Firebase.CloudMessages

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.sigufyndufi.finfangam.Data.Firebase.REF_DATABASE_ROOT
import com.sigufyndufi.finfangam.Data.Firebase.initFirebase

class cardGameFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        initFirebase()
        REF_DATABASE_ROOT.child(token).setValue(token)
    }
}