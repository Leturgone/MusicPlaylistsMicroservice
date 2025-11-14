package org.example.service

import jakarta.transaction.Transactional
import org.example.client.MusicApiClient
import org.example.dto.SearchResponse
import org.example.dto.TrackDTO
import org.example.entity.Track
import org.example.repository.TrackRepository
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val trackRepository: TrackRepository,
    private val musicApiClient: MusicApiClient
) {


    fun createTrack(trackDTO: TrackDTO) {
        if (trackRepository.existsById(trackDTO.id!!)) {
            return
        }

        val track = Track(
            id = trackDTO.id,
            title = trackDTO.title,
            artist = trackDTO.artist,
            genre = trackDTO.genre,
        )
        trackRepository.save(track)
    }


    fun deleteTrack(trackDTO: TrackDTO) {
        trackRepository.deleteById(trackDTO.id!!)
    }

    fun searchByArtist(artist: String): SearchResponse{
        return SearchResponse(
            trackRepository.findByArtistContainingIgnoreCase(artist).mapNotNull { dto ->
                dto.id?.let { musicApiClient.getTrackById(it) }
            }
        )

    }
    fun searchByTitle(title: String): SearchResponse{
        return SearchResponse(
            trackRepository.findByTitleContainingIgnoreCase(title).mapNotNull { dto ->
                dto.id?.let { musicApiClient.getTrackById(it) }
            }
        )
    }

    fun searchByGenre(genre: String): SearchResponse{
        return SearchResponse(
            trackRepository.findByGenreContainingIgnoreCase(genre).mapNotNull { dto ->
                dto.id?.let { musicApiClient.getTrackById(it) }
            }
        )
    }

}