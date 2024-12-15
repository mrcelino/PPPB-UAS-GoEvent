package com.example.uaspppb.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uaspppb.model.Event

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("SELECT * FROM favorite_events")
    suspend fun getAllEvents(): List<EventEntity>

    @Delete
    suspend fun deleteEvent(event : EventEntity)

    @Query("DELETE FROM favorite_events")
    suspend fun deleteAllEvents()


    @Query("DELETE FROM favorite_events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: String)
}
