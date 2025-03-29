package com.example.edushare_new.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.edushare_new.data.local.AppDatabase
import com.example.edushare_new.data.local.model.Post
import com.example.edushare_new.data.remote.ApiService
import com.example.edushare_new.data.remote.model.ZenQuote

class EduRepository(private val context: Context, private val apiService: ApiService) {

    private val db = AppDatabase.getInstance(context)
    private val postDao = db.postDao()

    // Room operations
    fun getAllPosts(): LiveData<List<Post>> = postDao.getAllPosts()
    fun getPostsByUser(userId: String): LiveData<List<Post>> = postDao.getPostsByUser(userId)

    suspend fun insertPost(post: Post) = postDao.insertPost(post)
    suspend fun updatePost(post: Post) = postDao.updatePost(post)
    suspend fun deletePost(post: Post) = postDao.deletePost(post)

    // Retrofit operation – fetch inspiration quotes
    suspend fun fetchQuotes(): List<ZenQuote> {
        return apiService.getRandomQuote()
    }
}
