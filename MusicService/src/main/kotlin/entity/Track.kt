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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 255)
    val title: String,

    @Column(length = 255)
    val artist: String? = null,

    @Column(length = 255)
    val genre: String? = null,

    @Column(name = "duration_sec")
    val durationSec: Int? = null,

    @Column(name = "owner_id")
    val ownerId: Long? = null,

    @Column(name = "size_bytes")
    val sizeBytes: Long? = null,

    @Column(name = "storage_path", length = 1024)
    val storagePath: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null
)