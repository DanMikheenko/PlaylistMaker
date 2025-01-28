package com.practicum.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.player.data.db.entity.AddedToPlaylistTrackEntity

@Dao
interface AddedToPlaylistTrackDao {
    @Insert(entity = AddedToPlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insert(track: AddedToPlaylistTrackEntity)
}