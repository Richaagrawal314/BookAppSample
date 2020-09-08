package com.internshala.bookhun.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val book_id: String,
    @ColumnInfo(name = "book_name") val bookNameEnCl: String,
    @ColumnInfo(name = "book_author") val bookAuthorEnCl: String,
  //  @ColumnInfo(name = "book_price") val bookPriceEnCl: String,
  //  @ColumnInfo(name = "book_rating") val bookRatingEnCl: String,
    @ColumnInfo(name = "book_description") val bookDescEnCl: String,
    @ColumnInfo(name = "book_image") val bookImageEnCl: String
)
