package com.akshay.readers_books_app.model

data class MUser(
    val id: String?,
    val userId: String,
    val display_name: String,
    val quote: String,
    val profession: String,
) {
    fun toMap(): MutableMap<String, Any> {
      return mutableMapOf("user_id" to this.userId,
          "display_name" to this.display_name,
        "quote" to this.quote,
        "profession" to this.profession,
      )
    }
}
