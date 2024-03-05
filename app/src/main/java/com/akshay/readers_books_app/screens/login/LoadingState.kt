package com.akshay.readers_books_app.screens.login

data class LoadingState(
    val status: Status,
    val msg: String? =null
) {

    companion object {
        val IDLE= LoadingState(Status.IDLE)
        val SUCCESS= LoadingState(Status.SUCCESS)
        val LOADING= LoadingState(Status.LOADING)
        val FAILED= LoadingState(Status.FAILED)
    }

    enum class Status {
        SUCCESS,
        FAILED,
        LOADING,
        IDLE
    }
}



