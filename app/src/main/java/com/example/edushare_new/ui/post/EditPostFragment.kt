package com.example.edushare_new.ui.post

import android.app.Activity
import android.app.AlertDialog
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
import com.example.edushare_new.databinding.FragmentEditPostBinding
import com.example.edushare_new.data.local.model.Post
import com.example.edushare_new.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*

class EditPostFragment : Fragment() {

    private lateinit var binding: FragmentEditPostBinding
    private lateinit var postViewModel: PostViewModel
    private var currentPost: Post? = null

    private val IMAGE_PICK_CODE = 1001
    private val PDF_PICK_CODE = 1002

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        val postId = EditPostFragmentArgs.fromBundle(requireArguments()).postId

        postViewModel.posts.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId }
            currentPost = post
            if (post != null) {
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (post.userId != currentUserId) {
                    Toast.makeText(requireContext(), "You are not allowed to edit this post.", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    return@observe
                }
                binding.etTitle.setText(post.title)
                binding.etDescription.setText(post.description)
                binding.etCategory.setText(post.category)
                if (post.imageUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(post.imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .into(binding.imageView)
                }
                if (!post.pdfUrl.isNullOrEmpty()) {
                    binding.tvPdfName.text = Uri.parse(post.pdfUrl).lastPathSegment
                }
            } else {
                Toast.makeText(requireContext(), "Post not found", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.btnSelectPdf.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, PDF_PICK_CODE)
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val category = binding.etCategory.text.toString().trim()

            if (title.isEmpty() || description.isEmpty() || category.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            currentPost?.let { post ->
                val updatedPost = post.copy(
                    title = title,
                    description = description,
                    category = category,
                    imageUrl = if (binding.imageView.tag != null) binding.imageView.tag as String else post.imageUrl,
                    pdfUrl = if (binding.tvPdfName.text.toString() != "No PDF selected") binding.tvPdfName.text.toString() else post.pdfUrl,
                    timestamp = Date().time
                )
                postViewModel.updatePost(updatedPost)
                Toast.makeText(requireContext(), "Post updated successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Post")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Yes") { _, _ ->
                    currentPost?.let { post ->
                        postViewModel.deletePost(post)
                        Toast.makeText(requireContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    val selectedImageUri = data?.data
                    selectedImageUri?.let {
                        binding.imageView.setImageURI(it)
                        // Save the image URI (as a String) in the tag for later update
                        binding.imageView.tag = it.toString()
                    }
                }
                PDF_PICK_CODE -> {
                    val selectedPdfUri = data?.data
                    selectedPdfUri?.let {
                        binding.tvPdfName.text = it.lastPathSegment ?: "PDF selected"
                    }
                }
            }
        }
    }
}
