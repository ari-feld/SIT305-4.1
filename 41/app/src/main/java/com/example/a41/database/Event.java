package com.example.a41.database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event implements java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String category;
    public String location;
    public long dateTime;

    public Event(String title, String category, String location, long dateTime) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.dateTime = dateTime;
    }

}
