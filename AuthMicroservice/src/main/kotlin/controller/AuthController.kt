package org.example.controller

import org.example.dto.LoginRequest
import org.example.dto.RegisterRequest
import org.example.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Long> {
        val  response = authService.register(request.email, request.password)
        if (response!=null) return ResponseEntity.ok(response)
        return ResponseEntity.badRequest().build()
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Long> {
        val response =  authService.login(request.email, request.password)
        if (response!=null) return ResponseEntity.ok(response)
        return ResponseEntity.badRequest().build()

    }
}