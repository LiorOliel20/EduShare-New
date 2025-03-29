package com.example.edushare_new.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edushare_new.databinding.FragmentHomeBinding
import com.example.edushare_new.viewmodel.HomeViewModel
import com.example.edushare_new.viewmodel.PostViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postsAdapter = PostsAdapter { postId ->
            val action = HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(postId)
            findNavController().navigate(action)
        }
        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postsAdapter
        }

        postViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postsAdapter.submitList(posts)
        }

        binding.btnAddPost.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreatePostFragment())
        }
    }
}
