package com.practicum.playlistmaker.search.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun delete(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks_table")
    fun getFavoriteTracks(): Flow<List<TrackEntity>?>

    @Query("SELECT trackId FROM favorite_tracks_table")
    fun getFavoriteTracksIds(): Flow<List<String>>
}