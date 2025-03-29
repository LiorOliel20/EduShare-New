package com.example.edushare_new.ui.post

import android.app.Activity
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
import com.example.edushare_new.data.local.model.Post
import com.example.edushare_new.databinding.FragmentCreatePostBinding
import com.example.edushare_new.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class CreatePostFragment : Fragment() {
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var viewModel: PostViewModel
    private var selectedImageUri: Uri? = null
    private var selectedPdfUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1001
        private const val PDF_PICK_CODE = 1002
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val category = binding.etCategory.text.toString().trim()
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId.isNullOrEmpty() || title.isEmpty() || description.isEmpty() || category.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val post = Post(
                userId = userId,
                title = title,
                description = description,
                imageUrl = selectedImageUri?.toString() ?: "",
                pdfUrl = selectedPdfUri?.toString(),
                category = category,
                timestamp = Date().time
            )

            viewModel.addPost(post)
            Toast.makeText(requireContext(), "Post added", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    selectedImageUri = data?.data
                    binding.imageView.setImageURI(selectedImageUri)
                }
                PDF_PICK_CODE -> {
                    selectedPdfUri = data?.data
                    binding.tvPdfName.text = selectedPdfUri?.lastPathSegment ?: "PDF selected"
                }
            }
        }
    }
}
