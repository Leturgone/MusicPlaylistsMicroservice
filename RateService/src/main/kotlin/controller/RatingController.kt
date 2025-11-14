package org.example.controller

import org.example.dto.DeleteRatingRequest
import org.example.dto.RatingResponse
import org.example.dto.RegisterDownloadRequest
import org.example.dto.SetRatingRequest
import org.example.service.RatingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rating")
class RatingController(
    private val ratingService: RatingService
) {
    // Добавление/Обновление оценки
    @PostMapping()
    fun setRating(@RequestBody request: SetRatingRequest): ResponseEntity<Long> {
        val ratingId = ratingService.setRating(request.trackId, request.stars, request.userId)
        return ResponseEntity.ok(ratingId)
    }

    // Удаление оценки
    @DeleteMapping()
    fun deleteRating(@RequestBody request: DeleteRatingRequest): ResponseEntity<Void> {
        val success = ratingService.deleteRating(request.trackId, request.userId)
        return if (success) ResponseEntity.ok().build()  else ResponseEntity.notFound().build()
    }


    @GetMapping("/avg/{trackId}")
    fun getRatingAvg(@PathVariable trackId: Long): ResponseEntity<RatingResponse?> {
        val response = ratingService.getRatingAvg(trackId)
        return ResponseEntity.ok(response)
    }

    // Учет прослушивания
    @PostMapping("/download")
    fun registerDownload(@RequestBody request: RegisterDownloadRequest): ResponseEntity<Void> {
        val success = ratingService.registerDownload(request.trackId)
        return if (success) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()
    }


}