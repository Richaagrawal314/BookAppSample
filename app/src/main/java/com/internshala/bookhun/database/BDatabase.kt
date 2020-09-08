package com.internshala.bookhun.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class],version = 1)
abstract class BDatabase : RoomDatabase() {

    abstract fun bookDao():BookDao
}