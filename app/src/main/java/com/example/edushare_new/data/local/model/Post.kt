package com.example.edushare_new.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val pdfUrl: String?,
    val category: String,
    val timestamp: Long,
)