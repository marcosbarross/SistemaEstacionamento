package com.example.parkingsystem.views.usuario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean>
        get() = _isLogged

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int>
        get() = _userId

    init {
        _isLogged.value = false
        _userId.value = Int.MIN_VALUE
    }

    fun setLogged(logged: Boolean) {
        _isLogged.value = logged
    }

    fun setUserId(userId: Int) {
        _userId.value = userId
    }
}
