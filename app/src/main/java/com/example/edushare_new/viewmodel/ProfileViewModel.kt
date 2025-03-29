package com.example.edushare_new.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.edushare_new.data.local.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUser?.let {
            _user.value = User(
                uid = it.uid,
                displayName = it.displayName ?: "",
                email = it.email ?: "",
                photoUrl = it.photoUrl?.toString()
            )
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _user.value = null
    }


    fun updateDisplayName(name: String, onResult: (Boolean, String?) -> Unit) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _user.value = _user.value?.copy(displayName = name)
                        onResult(true, null)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        } else {
            onResult(false, "User not logged in")
        }
    }


    fun updatePhotoUrl(photo: String, onResult: (Boolean, String?) -> Unit) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photo))
                .build()
            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _user.value = _user.value?.copy(photoUrl = photo)
                        onResult(true, null)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        } else {
            onResult(false, "User not logged in")
        }
    }
}
