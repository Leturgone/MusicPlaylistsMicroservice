package org.example.event

import org.example.dto.TrackDTO

data class TrackEvent (
    val type: String,
    val track: TrackDTO
)