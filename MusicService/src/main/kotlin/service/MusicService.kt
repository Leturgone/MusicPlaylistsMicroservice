package org.example.service

import org.example.client.RatingApiClient
import org.example.dto.TrackDTO
import org.example.dto.TrackMetadataResponse
import org.example.entity.Track
import org.example.repository.TrackRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class MusicService(
    private val trackRepository: TrackRepository,
    private val ratingClient: RatingApiClient,
    private val recommendationNotifier: RecommendationNotifier,
    private val trackEventPublisher: TrackEventPublisher,
    private val fileStorageService: FileStorageService
) {

    // Метод для получения метаданных трека с рейтингом
    fun getTrack(trackId: Long): TrackMetadataResponse? {
        val trackOpt = trackRepository.findById(trackId)
        if (trackOpt.isEmpty) return null
        val track = trackOpt.get()

        // Вызов оценки
        val ratingResponse = ratingClient.getRatingAvg(trackId)
        val ratingAvg = ratingResponse?.avg
        val ratingCount = ratingResponse?.count

        return TrackMetadataResponse(
            id = track.id,
            title = track.title,
            artist = track.artist,
            durationSec = track.durationSec,
            sizeBytes = track.sizeBytes,
            createdAt = track.createdAt,
            ratingAvg = ratingAvg,
            ratingCount = ratingCount)
    }

    fun getTracksByGenre(genre: String): List<Long>?{
        val trackList = trackRepository.findByGenre(genre)
        return trackList?.map { it.id as Long }
    }
    // Загрузка файла и создание записи
    fun uploadTrack(file: MultipartFile, title: String, artist: String, genre: String, durationSec: Int, ownerId: Long): Long? {
        val size = file.size
        val storagePath = fileStorageService.saveFile(file)

        val track = Track(
            title = title,
            artist = artist,
            genre = genre,
            durationSec = durationSec,
            ownerId = ownerId,
            sizeBytes = size,
            storagePath = storagePath,
        )
        val saved = trackRepository.save(track)
        saved.id?.let { ratingClient.setRating(it) }

        trackEventPublisher.publishTrackCreated(saved.toDTO())

        return saved.id
    }


    // Скачивание
    fun getTrackFile(userid:Long, trackId: Long): ByteArray? {
        val trackOpt = trackRepository.findById(trackId)
        if (trackOpt.isEmpty) return null
        val track = trackOpt.get()
        track.genre?.let {
            recommendationNotifier.notify(userid,it)}
        track.id?.let { ratingClient.registerDownload(it) }
        return track.storagePath?.let {
            fileStorageService.readFile(it)
        }
    }

    // Удаление
    fun deleteTrack(trackId: Long, requesterId: Long, isAdmin: Boolean): Boolean {
        val trackOpt = trackRepository.findById(trackId)
        if (trackOpt.isEmpty) return false
        val track = trackOpt.get()

        if (track.ownerId != requesterId && !isAdmin) return false

        trackRepository.delete(track)

        trackEventPublisher.publishTrackDeleted(track.toDTO())

        track.storagePath?.let { fileStorageService.deleteFile(it)}

        return true
    }
}

private fun Track.toDTO(): TrackDTO {
    return TrackDTO(id = this.id,
        title = this.title,
        artist = this.artist?:"unnamed",
        genre = this.genre,
        durationSec = this.durationSec)
}
