package com.internshala.bookhun.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun insertBook(bookEt: BookEntity)

    @Delete
    fun deleteBook(bookEt: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE book_id=:bookId")
    fun getBookById(bookId: String): BookEntity
}