package com.example.edushare_new.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.edushare_new.databinding.FragmentProfileBinding
import com.example.edushare_new.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private val IMAGE_PICK_CODE_PROFILE = 1003

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvUserName.text = user.displayName ?: "User"
                binding.tvEmail.text = user.email ?: "No Email"
                Picasso.get()
                    .load(user.photoUrl)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .into(binding.ivProfile)
            }
        }

        // הוספת מאזין ללחיצה על תמונת הפרופיל לצורך עדכון
        binding.ivProfile.setOnClickListener {
            // פתיחת Intent לבחירת תמונה מהגלריה
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE_PROFILE)
        }

        binding.btnEditProfile.setOnClickListener {
            showEditDialog()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE_PROFILE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let {
                // עדכון התמונה במסך
                binding.ivProfile.setImageURI(it)
                // קריאה לעדכון התמונה במודל (ייתכן וצריך להוסיף טיפול בשרת/ Firebase)
                viewModel.updatePhotoUrl(it.toString())
            }
        }
    }

    private fun showEditDialog() {
        val editText = EditText(requireContext())
        editText.hint = "Enter new display name"

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Display Name")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val name = editText.text.toString().trim()
                if (name.isNotEmpty()) {
                    viewModel.updateDisplayName(name)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

