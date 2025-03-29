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
import com.example.edushare_new.databinding.FragmentPostDetailBinding
import com.example.edushare_new.viewmodel.PostViewModel
import com.squareup.picasso.Picasso

class PostDetailFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailBinding
    private val args: PostDetailFragmentArgs by lazy { PostDetailFragmentArgs.fromBundle(requireArguments()) }
    private lateinit var postViewModel: PostViewModel

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
            } else {
                Toast.makeText(requireContext(), "Post not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
