package com.practicum.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.player.data.db.entity.AddedToPlaylistTrackEntity
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddedToPlaylistTrackDao {
    @Insert(entity = AddedToPlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insert(track: AddedToPlaylistTrackEntity)

    @Query("SELECT * FROM added_to_playlist_tracks_table WHERE trackId = :trackId")
    fun getTrackById(trackId: Int): Flow<TrackEntity?>

    @Delete(entity = TrackEntity::class)
    fun removeTrackFromPlaylist(track: TrackEntity)
}