package ru.michaelismur.androiddevtest.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.michaelismur.androiddevtest.utils.ViewModelFactory
import ru.michaelismur.androiddevtest.databinding.FragmentBinding
import ru.michaelismur.androiddevtest.web.ApiProvider
import ru.michaelismur.androiddevtest.web.PostsApi

class PostsFragment : Fragment() {

    private lateinit var viewModel: GifPostViewModel
    private lateinit var fragmentBinding: FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentBinding.inflate(inflater, container, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        observePost()
        observeIsPreviousPostExists()
        observeError()
        observeLoadingState()

        fragmentBinding.errorRoot.visibility = View.GONE

        fragmentBinding.nextPost.setOnClickListener {
            viewModel.nextPost()
        }
        fragmentBinding.previousPost.setOnClickListener {
            viewModel.previousPost()
        }

        fragmentBinding.retryButton.setOnClickListener {
            viewModel.getPost()
        }
    }

    private fun observePost() {
        viewModel.post.observe(viewLifecycleOwner, { post ->
            fragmentBinding.postView.visibility = View.VISIBLE
            fragmentBinding.errorRoot.visibility = View.GONE

            if (post == null) {
                fragmentBinding.postDescription.text = "А постов больше нет..."
            } else post.run {
                fragmentBinding.loading.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .asGif()
                    .load(
                        //Кушает только https
                        gifURL?.replace(
                            "http://",
                            "https://"
                        )
                    )
                    .listener(object : RequestListener<GifDrawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Toast.makeText(
                                context,
                                "Загрузить не удалось",
                                Toast.LENGTH_SHORT
                            ).show()
                            return false
                        }

                        override fun onResourceReady(
                            resource: GifDrawable?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            fragmentBinding.loading.visibility = View.GONE
                            return false
                        }
                    })
                    .override(250)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(fragmentBinding.post)

                fragmentBinding.postDescription.text = post.description
            }
        })
    }

    private fun observeLoadingState() {
        viewModel.loading.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    fragmentBinding.loading.visibility = View.VISIBLE
                }
                else -> {
                    fragmentBinding.loading.visibility = View.GONE
                }
            }
        })
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner, { error ->
            when (error?.networkError) {
                true -> {
                    fragmentBinding.postView.visibility = View.INVISIBLE
                    fragmentBinding.errorRoot.visibility = View.VISIBLE
                }
                else -> {
                    if (error?.error != null)
                        Toast.makeText(requireContext(), error.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
        fragmentBinding.loading.visibility = View.GONE
    }

    private fun observeIsPreviousPostExists() {
        viewModel.isPreviousPostExists.observe(viewLifecycleOwner, { prevPostAvailable ->
            if (prevPostAvailable) {
                fragmentBinding.previousPost.visibility = View.VISIBLE
            } else {
                fragmentBinding.previousPost.visibility = View.INVISIBLE
            }
        })
    }

    private fun setupViewModel() {
        ViewModelProvider(
            this,
            ViewModelFactory(PostsApi(ApiProvider.apiService))
        )[GifPostViewModel::class.java].also { viewModel = it }
        viewModel.getPost()
    }
}