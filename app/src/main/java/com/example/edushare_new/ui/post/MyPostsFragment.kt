package com.example.edushare_new.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edushare_new.R
import com.example.edushare_new.databinding.FragmentMyPostsBinding
import com.example.edushare_new.ui.home.PostsAdapter
import com.example.edushare_new.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth

class MyPostsFragment : Fragment() {

    private lateinit var binding: FragmentMyPostsBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter with a click listener for navigating to post details
        postsAdapter = PostsAdapter { postId ->
            val action = MyPostsFragmentDirections.actionMyPostsFragmentToPostDetailFragment(postId)
            findNavController().navigate(action)
        }
        binding.rvMyPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postsAdapter
        }

        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            postViewModel.getPostsByUser(currentUserId).observe(viewLifecycleOwner) { posts ->
                postsAdapter.submitList(posts)
            }
        } else {
            Toast.makeText(requireContext(), "User not logged in. Please log in.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}
