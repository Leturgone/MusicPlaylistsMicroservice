package org.example.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "track")
data class Track(
    @Id
    val id: Long? = null,

    @Column(nullable = false, length = 255)
    val title: String,

    @Column(length = 255)
    val artist: String? = null,

    @Column(length = 255)
    val genre: String? = null,
)