package com.example.edushare_new.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _user = MutableLiveData<FirebaseUser?>().apply {
        value = firebaseAuth.currentUser
    }
    val user: LiveData<FirebaseUser?> get() = _user

    fun updateProfile(displayName: String?, photoUri: Uri?, onResult: (Boolean, String?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val request = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(photoUri)
                .build()

            currentUser.updateProfile(request)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _user.value = firebaseAuth.currentUser
                        onResult(true, null)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        } else {
            onResult(false, "No user is logged in")
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        _user.value = null
    }
}
