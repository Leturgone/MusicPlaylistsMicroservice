package org.example.controller

import org.example.dto.CreateProfileRequest
import org.example.dto.ProfileDTO
import org.example.dto.ProfileRespond
import org.example.entites.Profile
import org.example.servicies.ProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/profile")
class ProfileController(
    private val profileService: ProfileService
) {

    @GetMapping("/me")
    fun getMyProfile(@RequestHeader("X-User-Id") userId: Long): ResponseEntity<ProfileRespond> {
        val profile = profileService.getProfile(userId)
        if (profile==null) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/{userId}")
    fun getProfileName(@PathVariable userId: Long): ResponseEntity<String> {
        val name = profileService.getProfileName(userId)
        if (name==null) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(name)
    }

    @PostMapping
    fun createProfile(@RequestBody profile: CreateProfileRequest): ResponseEntity<Profile> {
        val createdProfile = profileService.createProfile(profile)
        return ResponseEntity.ok(createdProfile)
    }

    @PatchMapping
    fun updateProfile(
        @RequestHeader("X-User-Id") userId: Long,
        @RequestBody dto: ProfileDTO
    ): ResponseEntity<Void> {
        val updatedProfile = profileService.updateProfile(userId, dto)
        if (!updatedProfile) return ResponseEntity.notFound().build()

        return ResponseEntity.ok().build()
    }

}