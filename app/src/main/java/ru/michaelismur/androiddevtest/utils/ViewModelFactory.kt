package ru.michaelismur.androiddevtest.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.michaelismur.androiddevtest.web.PostsApi
import ru.michaelismur.androiddevtest.posts.GifPostViewModel

class ViewModelFactory(private val postsApi: PostsApi) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GifPostViewModel::class.java)) return GifPostViewModel(
            postsApi
        ) as T
        else throw IllegalArgumentException("ViewModel was not defined")
    }
}