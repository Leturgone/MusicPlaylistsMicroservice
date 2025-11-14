package org.example.repository

import org.example.entity.Track
import org.springframework.data.repository.CrudRepository

interface TrackRepository : CrudRepository<Track, Long> {

    fun findByArtistContainingIgnoreCase(artist: String): List<Track>

    fun findByTitleContainingIgnoreCase(title: String): List<Track>

    fun findByGenreContainingIgnoreCase(gene: String): List<Track>
}