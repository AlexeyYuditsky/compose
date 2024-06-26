package com.alexeyyuditsky.vkclient.data.repository

import android.app.Application
import com.alexeyyuditsky.vkclient.data.mapper.NewsFeedMapper
import com.alexeyyuditsky.vkclient.data.network.ApiFactory
import com.alexeyyuditsky.vkclient.data.network.ApiService
import com.alexeyyuditsky.vkclient.domain.FeedPost
import com.alexeyyuditsky.vkclient.domain.StatisticType

class NewsFeedRepository(
    application: Application,
    private val apiService: ApiService = ApiFactory.apiService
) : BaseRepository(application) {

    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts get() = _feedPosts.toList()

    private var nextFrom: String? = null

    suspend fun loadRecommendations(): List<FeedPost> {
        val startFrom = nextFrom
        // the situation when on the server end news
        if (startFrom == null && feedPosts.isNotEmpty()) return feedPosts

        val response = if (startFrom == null) {
            apiService.loadRecommendations(token = getAccessToken())
        } else {
            apiService.loadRecommendations(
                token = getAccessToken(),
                nextFrom = startFrom
            )
        }
        nextFrom = response.newsFeedContent.nextFrom
        val feedPostList = mapper.mapResponseToPosts(response)
        _feedPosts.addAll(feedPostList)

        return feedPosts
    }

    suspend fun changeLikeStatus(feedPost: FeedPost): List<FeedPost> {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count

        val newStatistics = feedPost.statistics.map { statisticItem ->
            if (statisticItem.type == StatisticType.LIKES) {
                statisticItem.copy(count = newLikesCount)
            } else {
                statisticItem
            }
        }

        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val feedPostIndex = feedPosts.indexOf(feedPost)
        _feedPosts[feedPostIndex] = newPost
        return feedPosts
    }

    suspend fun ignorePost(feedPost: FeedPost): List<FeedPost> {
        apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)
        return feedPosts
    }
}