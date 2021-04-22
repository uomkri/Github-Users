package ru.uomkri.tchktest.screens.auth

import androidx.lifecycle.ViewModel
import ru.uomkri.tchktest.repo.Repository

class AuthViewModel : ViewModel() {

    val success = Repository.authSuccess

}