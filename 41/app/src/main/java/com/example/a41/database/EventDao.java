package com.example.a41.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    // CREATE
    @Insert
    void insert(Event event);

    // READ (sorted by date)
    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    List<Event> getAllEvents();

    // UPDATE
    @Update
    void update(Event event);

    // DELETE
    @Delete
    void delete(Event event);
}