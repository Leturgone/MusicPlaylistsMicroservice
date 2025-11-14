package org.example.service

import org.example.entities.User
import org.example.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
) {

    fun register(email: String, password: String): Long? {
        val existUser = userRepository.findByEmail(email)
        if (existUser!=null) return null
        val user = User(email = email, passwordHash = passwordEncoder.encode(password))
        val saved = userRepository.save(user)
        return saved.id

    }

    fun login(email: String, password: String): Long? {
        val user = userRepository.findByEmail(email)
        if (user == null ||!passwordEncoder.matches(password, user.passwordHash)) {
            return null
        }
        return user.id
    }
}