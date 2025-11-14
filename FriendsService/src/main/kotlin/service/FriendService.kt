package org.example.service

import org.example.client.ProfileApiClient
import org.example.dto.FriendDTO
import org.example.dto.FriendListRespond
import org.example.entities.Friend
import org.example.entities.FriendId
import org.example.repository.FriendRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class FriendService(
    private val friendRepository: FriendRepository,
    private val profileApiClient: ProfileApiClient
) {
    fun addFriend(userId: Long, friendUserId: Long): Boolean {
        if (profileApiClient.getProfileNameById(friendUserId)==null) return false

        val id = FriendId(userId, friendUserId)

        if (friendRepository.existsById(id)) return false

        val friend = Friend(id = id, createdAt = LocalDateTime.now())
        friendRepository.save(friend)
        return true
    }

    fun removeFriend(userId: Long, friendUserId: Long): Boolean {
        val id = FriendId(userId, friendUserId)

        friendRepository.deleteById(id)
        return true
    }

    fun getFriends(userId: Long): FriendListRespond {
        val list = friendRepository.findAllByIdUserId(userId).mapNotNull { it ->
            val name = profileApiClient.getProfileNameById(it.id.friendUserId)
            if (name == null) {
                null
            } else {
                FriendDTO(it.id.friendUserId, name)
            }
        }
        return FriendListRespond(list)
    }
}