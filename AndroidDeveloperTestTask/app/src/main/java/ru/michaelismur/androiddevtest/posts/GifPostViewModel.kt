package ru.michaelismur.androiddevtest.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.michaelismur.androiddevtest.model.PostInfo
import ru.michaelismur.androiddevtest.model.ResponseWrapper
import ru.michaelismur.androiddevtest.web.PostsApi

class GifPostViewModel(private val api: PostsApi) : ViewModel() {
    private var postIndex = 0
    private val displayedPosts = mutableListOf<PostInfo>()

    private val _isPreviousPostExists: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    private val _post: MutableLiveData<PostInfo?> by lazy {
        MutableLiveData()
    }

    private val _loading: MutableLiveData<Boolean?> by lazy {
        MutableLiveData()
    }

    private val _error: MutableLiveData<ResponseWrapper.Failure?> by lazy {
        MutableLiveData()
    }


    val post: LiveData<PostInfo?>
        get() = _post
    val isPreviousPostExists: LiveData<Boolean>
        get() = _isPreviousPostExists
    val loading: LiveData<Boolean?>
        get() = _loading
    val error: LiveData<ResponseWrapper.Failure?>
        get() = _error

    fun getPost() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = api.getRandomPost()) {
                is ResponseWrapper.Success -> {
                    _loading.value = false
                    if (!displayedPosts.contains(result.value)
                    ) {
                        displayedPosts.add(result.value)
                    }
                    postChanged()
                    _error.value = null
                }
                is ResponseWrapper.Failure -> {
                    _loading.value = false
                    _error.value = result
                }
            }
        }
    }

    fun nextPost() {
        if (++postIndex >= displayedPosts.size) {
            getPost()
        } else {
            postChanged()
        }
    }

    fun previousPost() {
        if (postIndex > 0) {
            postIndex--
            postChanged()
        }
    }

    private fun postChanged() {
        if (postIndex < displayedPosts.size) _post.value =
            displayedPosts[postIndex]
        if (displayedPosts.size == 0) {
            _post.value = null
        }

        _isPreviousPostExists.value = postIndex != 0
    }
}
