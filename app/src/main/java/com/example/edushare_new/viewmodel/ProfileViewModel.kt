package com.example.edushare_new.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.edushare_new.data.local.model.User
import com.google.firebase.auth.FirebaseAuth

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

    fun updateDisplayName(name: String) {
        _user.value = _user.value?.copy(displayName = name)
    }

    fun updatePhotoUrl(photo: String) {
        _user.value = _user.value?.copy(photoUrl = photo)
    }
}

