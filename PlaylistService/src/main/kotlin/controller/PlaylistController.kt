package org.example.controller

import org.example.dto.AllPlaylistRespond
import org.example.dto.CreatePlaylistRequest
import org.example.dto.PlaylistDTO
import org.example.dto.PlaylistRespond
import org.example.dto.UpdatePlaylistRequest
import org.example.entity.Playlist
import org.example.service.PlaylistService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/playlists")
class PlaylistController(private val playlistService: PlaylistService) {

    @GetMapping("/{id}")
    fun getPlaylist(@PathVariable id: Long): ResponseEntity<PlaylistRespond> {
        val playlist = playlistService.getPlaylistWithTracks(id)
        if (playlist == null) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(playlist)
    }

    // Создание плейлиста
    @PostMapping
    fun createPlaylist(@RequestBody request: CreatePlaylistRequest): ResponseEntity<Map<String, Long>>{
        val id = playlistService.createPlaylist(request.title, request.ownerId, request.tracksIds)
        return if (id == null){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }else{
            ResponseEntity.ok(mapOf("id" to id))
        }
    }



    // Обновление названия и/или состава
    @PatchMapping("/{id}")
    fun updatePlaylist(@PathVariable id: Long,
                       @RequestHeader("X-User-Id") userId: Long,
                       @RequestBody request: UpdatePlaylistRequest): ResponseEntity<Void> {
        val respond = playlistService.updatePlaylist(id, request.title, request.trackIds,userId)
        return if (respond){
            ResponseEntity.ok().build()
        }else{
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    // Получение плейлиста рекомендаций
    @GetMapping("/recommendation/{userId}")
    fun getRecommendationPlayList(@PathVariable userId: Long):  ResponseEntity<PlaylistRespond> {
        val response = playlistService.getRecommendationPlayList(userId)
        return if (response == null){
            ResponseEntity.notFound().build()
        }else{
            ResponseEntity.ok(response)
        }
    }

    // Получение данных о плейлисте без треков
    @GetMapping("meta/{playlistId}")
    fun getPlaylistMetadata(@PathVariable playlistId: Long): ResponseEntity<Playlist> {
        val response = playlistService.getPlaylistMetadataById(playlistId)
        return if (response == null){
            ResponseEntity.notFound().build()
        }else{
            ResponseEntity.ok(response)
        }
    }

    @GetMapping("all/{userId}")
    fun getMyPlaylists(@PathVariable userId: Long): ResponseEntity<AllPlaylistRespond> {
        val playlists = playlistService.getAllPlaylists(userId)
        return ResponseEntity.ok(playlists)
    }

}