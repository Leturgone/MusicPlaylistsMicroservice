package org.example.controller

import org.example.dto.SearchResponse
import org.example.service.SearchService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController(
    private val  searchService: SearchService
) {
    // Поиск по исполнителю
    @GetMapping("/artist/{artist}")
    fun searchByArtist(@PathVariable artist: String): ResponseEntity<SearchResponse> {
        return try {
            val tracks = searchService.searchByArtist(artist)
            ResponseEntity.ok(tracks)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    // Поиск по названию
    @GetMapping("/title/{title}")
    fun searchByTitle(@PathVariable title: String): ResponseEntity<SearchResponse> {
        return try {
            val tracks = searchService.searchByTitle(title)
            ResponseEntity.ok(tracks)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    // Поиск по названию
    @GetMapping("/genre/{genre}")
    fun searchByGenre(@PathVariable genre: String): ResponseEntity<SearchResponse> {
        return try {
            val tracks = searchService.searchByGenre(genre)
            ResponseEntity.ok(tracks)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}