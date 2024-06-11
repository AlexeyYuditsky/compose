package com.alexeyyuditsky.vkclient.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexeyyuditsky.vkclient.domain.FeedPost

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FeedPostsScreen(
    paddingValues: PaddingValues,
    feedPosts: List<FeedPost>,
    onCommentsClickListener: (FeedPost) -> Unit
) {
    val viewModel = viewModel<NewsFeedViewModel>()

    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = feedPosts,
            key = { it.id }
        ) { feedPost ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { swipeValue ->
                    when (swipeValue) {
                        SwipeToDismissBoxValue.EndToStart -> {
                            viewModel.remove(feedPost)
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
            )

            SwipeToDismissBox(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                content = {
                    PostCard(
                        feedPost = feedPost,
                        onViewsClickListener = { statisticItem ->
                            viewModel.updateCount(feedPost, statisticItem)
                        },
                        onShareClickListener = { statisticItem ->
                            viewModel.updateCount(feedPost, statisticItem)
                        },
                        onCommentClickListener = { statisticItem ->
                            onCommentsClickListener(feedPost)
                        },
                        onLikeClickListener = { _ ->
                            viewModel.changeLikeStatus(feedPost)
                        },
                        isFavorite = feedPost.isLiked
                    )
                },
                backgroundContent = {},
                enableDismissFromEndToStart = true,
                enableDismissFromStartToEnd = false
            )
        }
    }
}

/*
@Composable
@Preview
private fun FeedPostsPreview() = VkClientTheme {
    FeedPostsScreen(
        paddingValues = PaddingValues(),
        feedPosts = listOf(FeedPost(0), FeedPost(1), FeedPost(2), FeedPost(3), FeedPost(4))
    ) {}
}*/
