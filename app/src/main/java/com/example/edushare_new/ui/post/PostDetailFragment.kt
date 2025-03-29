package com.example.edushare_new.ui.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.edushare_new.R
import com.example.edushare_new.data.local.model.Post
import com.example.edushare_new.databinding.FragmentPostDetailBinding
import com.example.edushare_new.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class PostDetailFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailBinding
    private val args: PostDetailFragmentArgs by lazy { PostDetailFragmentArgs.fromBundle(requireArguments()) }
    private lateinit var postViewModel: PostViewModel
    private var currentPost: Post? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        postViewModel.posts.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == args.postId }
            currentPost = post
            if (post != null) {
                binding.tvTitle.text = post.title
                binding.tvDescription.text = post.description

                if (post.imageUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(post.imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .into(binding.ivPostImage)
                } else {
                    binding.ivPostImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }

                binding.btnDownloadPDF.setOnClickListener {
                    if (!post.pdfUrl.isNullOrEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.pdfUrl))
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "PDF not available", Toast.LENGTH_SHORT).show()
                    }
                }

                // Show edit and delete buttons only if the current user is the owner of the post
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (post.userId == currentUserId) {
                    binding.btnEditPost.visibility = View.VISIBLE
                    binding.btnDeletePost.visibility = View.VISIBLE
                } else {
                    binding.btnEditPost.visibility = View.GONE
                    binding.btnDeletePost.visibility = View.GONE
                }

                binding.btnEditPost.setOnClickListener {
                    // Navigate to EditPostFragment, passing the postId via SafeArgs
                    val action = PostDetailFragmentDirections.actionPostDetailFragmentToEditPostFragment(post.id ?: -1)
                    findNavController().navigate(action)
                }

                binding.btnDeletePost.setOnClickListener {
                    androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Delete Post")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton("Yes") { _, _ ->
                            postViewModel.deletePost(post)
                            Toast.makeText(requireContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Post not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
