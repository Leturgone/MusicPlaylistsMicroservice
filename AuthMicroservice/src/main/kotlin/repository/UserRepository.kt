package org.example.repository

import jakarta.transaction.Transactional
import org.example.entities.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
    fun findByEmail(email: String): User?

    @Modifying
    @Query("UPDATE User u SET u.passwordHash = :passwordHash WHERE u.email = :email")
    @Transactional
    fun updatePasswordHash(email: String, passwordHash: String)

}