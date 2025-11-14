package org.example.repository

import org.example.entity.Track
import org.springframework.data.repository.CrudRepository

interface TrackRepository: CrudRepository<Track, Long> {
    fun findByGenre(genre: String): List<Track>?
}