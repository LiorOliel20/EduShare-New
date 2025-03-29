package com.example.edushare_new.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.edushare_new.data.local.model.Post
import com.example.edushare_new.data.remote.RetrofitInstance
import com.example.edushare_new.data.repository.EduRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EduRepository(application, RetrofitInstance.apiService)

    val allPosts: LiveData<List<Post>> = repository.getAllPosts()
}
