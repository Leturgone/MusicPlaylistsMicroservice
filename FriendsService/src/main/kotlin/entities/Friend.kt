package org.example.entities

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "friends")
data class Friend(
    @EmbeddedId
    val id: FriendId,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)