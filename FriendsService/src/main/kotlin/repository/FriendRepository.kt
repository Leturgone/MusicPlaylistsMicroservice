package org.example.repository

import org.example.entities.Friend
import org.example.entities.FriendId
import org.springframework.data.repository.CrudRepository

interface FriendRepository: CrudRepository<Friend, FriendId>{
    fun findAllByIdUserId(userId: Long): List<Friend>
}