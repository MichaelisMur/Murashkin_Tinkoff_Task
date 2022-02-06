package ru.michaelismur.androiddevtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.michaelismur.androiddevtest.databinding.ActivityMainBinding
import androidx.fragment.app.FragmentTransaction
import ru.michaelismur.androiddevtest.posts.PostsFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.parent_fragment_container, PostsFragment())
        ft.commit()
    }
}