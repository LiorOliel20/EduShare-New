package com.example.edushare_new.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.edushare_new.data.local.model.Post
import com.example.edushare_new.data.remote.RetrofitInstance
import com.example.edushare_new.data.repository.EduRepository
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EduRepository(application, RetrofitInstance.apiService)
    val posts = repository.getAllPosts()

    fun addPost(post: Post) {
        viewModelScope.launch {
            repository.insertPost(post)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            repository.updatePost(post)
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            repository.deletePost(post)
        }
    }

    fun getPostsByUser(userId: String): LiveData<List<Post>> {
        return repository.getPostsByUser(userId)
    }
}
