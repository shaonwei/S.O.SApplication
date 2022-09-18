package com.example.sosapplication

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact :Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("Select * from contactTable order by id ASC")
    fun getAllContact(): LiveData<List<Contact>>

    @Update
    suspend fun update(contact: Contact)
}