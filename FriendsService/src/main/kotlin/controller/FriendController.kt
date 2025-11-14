package org.example.controller

import org.example.dto.AddFriendRequest
import org.example.dto.FriendListRespond
import org.example.service.FriendService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friends")
class FriendController(
    private val friendService: FriendService,
) {

    @PostMapping
    fun addFriend(
        @RequestBody request: AddFriendRequest,
        @RequestHeader("X-User-Id") userId: Long
    ): ResponseEntity<Unit> {
        val  respond = friendService.addFriend(userId, request.friendUserId)
        if (respond) return ResponseEntity.ok().build()
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{friendUserId}")
    fun deleteFriend(
        @PathVariable friendUserId: Long,
        @RequestHeader("X-User-Id") userId: Long
    ): ResponseEntity<Unit> {
        val  respond = friendService.removeFriend(userId, friendUserId)
        if (respond) return ResponseEntity.ok().build()
        return ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getFriends(
        @RequestHeader("X-User-Id") userId: Long,
    ): ResponseEntity<FriendListRespond> {
        val friends = friendService.getFriends(userId)
        return ResponseEntity.ok(friends)
    }
}