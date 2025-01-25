package com.practicum.playlistmaker.search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.search.data.db.dao.TrackDao
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity

@Database(version = 4, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}