package com.manuel.movieapp.account

sealed class AccountStateResponse {
    class Success(val accountState: AccountState): AccountStateResponse()
    object Error: AccountStateResponse()
}