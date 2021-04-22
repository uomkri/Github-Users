package ru.uomkri.tchktest.screens.details

import androidx.lifecycle.ViewModel
import ru.uomkri.tchktest.repo.Repository

class DetailsViewModel : ViewModel() {

    val user = Repository.user
    val error = Repository.error

    fun getUser(login: String) = Repository.getUser(login)

    fun clearSelectedUser() = Repository.clearSelectedUser()
}