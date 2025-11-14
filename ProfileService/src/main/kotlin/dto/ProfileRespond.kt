package org.example.dto

data class ProfileRespond(
    val displayName: String?,
    val bio: String?,
    val playLists: List<PlaylistDTO>,
    val friends:List<FriendDTO>,
)