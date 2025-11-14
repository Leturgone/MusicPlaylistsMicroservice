package org.example.entities

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class FriendId(
    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "friend_user_id")
    val friendUserId: Long
)
