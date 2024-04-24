package com.alexeyyuditsky.vkclient.data.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id") val id: String,
    @SerializedName("source_id") val communityId: Long,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: Long,
    @SerializedName("attachments") val attachments: List<AttachmentDto>?,
    @SerializedName("views") val views: ViewsDto,
    @SerializedName("reposts") val reposts: RepostsDto,
    @SerializedName("comments") val comments: CommentsDto,
    @SerializedName("likes") val likes: LikesDto
)