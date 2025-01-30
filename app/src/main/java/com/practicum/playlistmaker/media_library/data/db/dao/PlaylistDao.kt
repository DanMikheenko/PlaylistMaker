package com.practicum.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    fun delete(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>?>

    @Query("SELECT * FROM playlists_table WHERE playlistId = :playlistId")
    fun getPlaylistById(playlistId: Int): Flow<PlaylistEntity?>
}