package com.example.edushare_new.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edushare_new.databinding.FragmentProfileBinding
import com.example.edushare_new.ui.home.PostsAdapter
import com.example.edushare_new.viewmodel.AuthViewModel
import com.example.edushare_new.viewmodel.PostViewModel
import com.example.edushare_new.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var postViewModel: PostViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var postsAdapter: PostsAdapter

    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1002
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvUserName.text = user.displayName ?: "User"
                Picasso.get()
                    .load(user.photoUrl)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .into(binding.ivProfile)
            }
        }

        postsAdapter = PostsAdapter { postId ->
            // Handle post click if needed
        }
        binding.rvUserPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postsAdapter
        }

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            postViewModel.getPostsByUser(currentUserId).observe(viewLifecycleOwner) { posts ->
                postsAdapter.submitList(posts)
            }
        }

        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.btnLogout.setOnClickListener {
            profileViewModel.logout()
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(android.R.layout.simple_list_item_1, null)
        val input = EditText(requireContext())
        input.hint = "Enter display name"
        dialogView.findViewById<ViewGroup>(android.R.id.text1)?.addView(input)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                profileViewModel.updateProfile(newName, selectedImageUri) { success, message ->
                    if (!success) {
                        // Show error if needed
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()

        openImagePicker()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let {
                binding.ivProfile.setImageURI(it)
            }
        }
    }
}
