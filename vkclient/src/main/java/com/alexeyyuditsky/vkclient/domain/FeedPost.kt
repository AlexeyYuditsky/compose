package com.alexeyyuditsky.vkclient.domain

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.alexeyyuditsky.vkclient.R
import com.alexeyyuditsky.vkclient.core.getCheckedParcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost(
    val id: Int = 0,
    val communityName: String = "/dev/null",
    val publicationDate: String = "14:00",
    val avatarResId: Int = R.drawable.post_comunity_thumbnail,
    val contentText: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    val contentImageResId: Int = R.drawable.post_content_image,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 966),
        StatisticItem(StatisticType.SHARES, 7),
        StatisticItem(StatisticType.COMMENTS, 8),
        StatisticItem(StatisticType.LIKES, 27)
    )
) : Parcelable {
    fun encode(): String {
        val feedPostJson = Gson().toJson(this)
        return Uri.encode(feedPostJson)
    }
}

val NavType.Companion.FeedPostType: NavType<FeedPost>
    get() = object : NavType<FeedPost>(false) {

        override fun get(bundle: Bundle, key: String): FeedPost =
            bundle.getCheckedParcelable(key, FeedPost::class.java)

        override fun parseValue(value: String): FeedPost =
            Gson().fromJson(value, FeedPost::class.java)

        override fun put(bundle: Bundle, key: String, value: FeedPost) =
            bundle.putParcelable(key, value)
    }