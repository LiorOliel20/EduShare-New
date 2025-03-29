package com.example.edushare_new.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.edushare_new.data.remote.RetrofitInstance
import com.example.edushare_new.data.remote.model.ZenQuote
import kotlinx.coroutines.launch

class InspirationViewModel(application: Application) : AndroidViewModel(application) {
    private val _quote = MutableLiveData<ZenQuote>()
    val quote: LiveData<ZenQuote> get() = _quote

    fun fetchRandomQuote() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getRandomQuote()
                _quote.value = response.firstOrNull()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}