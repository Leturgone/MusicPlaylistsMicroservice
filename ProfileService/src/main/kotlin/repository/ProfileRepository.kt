package org.example.repository

import org.example.entites.Profile
import org.springframework.data.repository.CrudRepository

interface ProfileRepository: CrudRepository<Profile, Long> {

}