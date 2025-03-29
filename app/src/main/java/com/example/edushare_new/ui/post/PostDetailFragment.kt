package com.example.edushare_new.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.edushare_new.databinding.FragmentPostDetailBinding
import com.squareup.picasso.Picasso

class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private val args: PostDetailFragmentArgs by lazy { PostDetailFragmentArgs.fromBundle(requireArguments()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // יש לממש קריאה ממסד הנתונים לקבלת פרטי הפוסט לפי args.postId
        binding.tvTitle.text = "כותרת הפוסט"
        binding.tvDescription.text = "תיאור הפוסט המלא"
        Picasso.get()
            .load("https://example.com/image.jpg") // החלף בכתובת תמונת הפוסט
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(binding.ivPostImage)

        binding.btnDownloadPDF.setOnClickListener {
            // מימוש הורדת קובץ PDF (אם קיים)
        }
    }
}
