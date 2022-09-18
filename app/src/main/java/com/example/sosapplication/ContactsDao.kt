package com.example.sosapplication

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(contact :Contact)

    @Delete
     fun delete(contact: Contact)

    @Query("Select * from contactTable order by id ASC")
    fun getAllContact(): LiveData<List<Contact>>

    @Update
     fun update(contact: Contact)
}