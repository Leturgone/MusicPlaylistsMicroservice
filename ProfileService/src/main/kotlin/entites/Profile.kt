package org.example.entites

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "profile")
data class Profile(
    @Id
    val userId: Long,

    @Column(name = "display_name")
    var displayName: String? = null,

    @Column(columnDefinition = "TEXT")
    var bio: String? = null,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
