package com.akshay.readers_books_app.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName


data class MBook(
    @Exclude var id: String? = null,
    var title: String? = null,
    var authors: String? = null,
    var notes: String? = null,
    @get:PropertyName("book_photo_url")
    @set:PropertyName("book_photo_url")
    var photoUrl: String? = null,
    var category: String? = null,
    @get:PropertyName("publish_data")
    @set:PropertyName("publish_date")
    var publishedDate: String? = null,
    var rating: Double? = null,
    var description: String? = null,
    @get:PropertyName("page_count")
    @set:PropertyName("page_count")
    var pageCount: String? = null,
    @get:PropertyName("started_reading_at")
    @set:PropertyName("started_reading_at")
    var startReading: Timestamp? = null,
    @get:PropertyName("finished_reading_at")
    @set:PropertyName("finished_reading_at")
    var finishReading: Timestamp? = null,
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,
    @get:PropertyName("google_books_id")
    @set:PropertyName("google_books_id")
    var googleBooksId: String? = null
)
