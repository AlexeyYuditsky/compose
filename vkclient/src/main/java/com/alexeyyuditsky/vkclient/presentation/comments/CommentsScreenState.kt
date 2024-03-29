package com.alexeyyuditsky.vkclient.presentation.comments

import com.alexeyyuditsky.vkclient.domain.FeedPost
import com.alexeyyuditsky.vkclient.domain.PostComment

sealed interface CommentsScreenState {

    object Initial : CommentsScreenState

    class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState
}