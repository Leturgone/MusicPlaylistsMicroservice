package org.example.servicies

import org.example.client.FriendsApiClient
import org.example.client.PlaylistApiClient
import org.example.dto.CreateProfileRequest
import org.example.dto.ProfileDTO
import org.example.dto.ProfileRespond
import org.example.entites.Profile
import org.example.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val friendsApiClient: FriendsApiClient,
    private val playlistApiClient: PlaylistApiClient
) {

    fun getProfile(userId: Long): ProfileRespond? {
        val profileOpt = profileRepository.findById(userId)
        if (profileOpt.isEmpty) return null

        val  profile = profileOpt.get()
        val friends = friendsApiClient.getFriends(userId)?.friends?:emptyList()
        val playlists= playlistApiClient.getPlaylists(userId)?.playlists?:emptyList()

        return ProfileRespond(
            displayName = profile.displayName,
            bio = profile.bio,
            playLists = playlists,
            friends = friends,
        )
    }

    fun createProfile(profile: CreateProfileRequest): Profile {
        val createdProfile = Profile(
            userId = profile.userId, displayName = profile.displayName, bio = profile.bio
        )
        return profileRepository.save(createdProfile)
    }

    fun  getProfileName(userId: Long): String?{
        val profileOpt = profileRepository.findById(userId)
        if (profileOpt.isEmpty) return null
        return profileOpt.get().displayName
    }

    fun updateProfile(userId: Long, dto: ProfileDTO): Boolean {
        val profileOpt = profileRepository.findById(userId)
        if (profileOpt.isEmpty) return false

        val  profile = profileOpt.get()
        dto.displayName?.let { profile.displayName = it }
        dto.bio?.let { profile.bio = it }
        profile.updatedAt = LocalDateTime.now()

        profileRepository.save(profile)
        return true
    }

}