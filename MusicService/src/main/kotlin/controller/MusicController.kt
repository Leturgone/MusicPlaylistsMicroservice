package org.example.controller

import org.example.dto.TrackMetadataResponse
import org.example.service.MusicService
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/music")
class MusicController(
    private val musicService: MusicService,
) {

    @GetMapping("/{trackId}")
    fun getTrack(@PathVariable trackId: Long): ResponseEntity<TrackMetadataResponse> {
        val meta = musicService.getTrack(trackId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(meta)
    }

    @PostMapping
    fun uploadTrack(
        @RequestPart("file") file: MultipartFile,
        @RequestParam("title") title: String,
        @RequestParam("artist") artist: String,
        @RequestParam("genre") genre: String,
        @RequestParam("durationSec") durationSec: Int,
        @RequestHeader("X-User-Id") userId: Long
    ): ResponseEntity<Map<String, Long>> {
        val trackId = musicService.uploadTrack(file, title, artist, genre,durationSec,userId)
        return  if (trackId == null){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }else{
            ResponseEntity.ok(mapOf("id" to trackId))
        }
    }

    @GetMapping("tracks/{genre}")
    fun getTracksByGenre(@PathVariable genre: String): ResponseEntity<List<Long>>{
        val trackList = musicService.getTracksByGenre(genre)?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(trackList)

    }
    @GetMapping("/{trackId}/download")
    fun downloadTrack(@PathVariable trackId: Long, @RequestHeader("X-User-Id") userId: Long): ResponseEntity<ByteArray> {
        val fileBytes = musicService.getTrackFile(userId, trackId) ?: return ResponseEntity.notFound().build()
        val trackMetadata = musicService.getTrack(trackId)?: return ResponseEntity.notFound().build()

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("audio/mp3")
        headers.contentDisposition = ContentDisposition.attachment().filename(trackMetadata.title).build()
        return ResponseEntity.ok().headers(headers).body(fileBytes)
    }

    @DeleteMapping("/{trackId}")
    fun deleteTrack(
        @PathVariable trackId: Long,
        @RequestHeader("X-User-Id") userId: Long,
        @RequestHeader("X-User-Role") role: String
    ): ResponseEntity<Void> {
        val isAdmin = role == "admin"
        val success = musicService.deleteTrack(trackId, userId, isAdmin)
        return if (success) ResponseEntity.ok().build() else ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }


}