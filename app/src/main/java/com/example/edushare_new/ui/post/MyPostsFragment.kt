package com.example.edushare_new.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

        // אתחול ה-Adapter עם מאזין ללחיצה על פריט (למעבר למסך פרטי הפוסט)
        postsAdapter = PostsAdapter { postId ->
            // שימוש ב-SafeArgs להעברת מזהה הפוסט למסך הפרטים
            val action = MyPostsFragmentDirections.actionMyPostsFragmentToPostDetailFragment(postId)
            findNavController().navigate(action)
        }
        binding.rvMyPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postsAdapter
        }

        // אתחול ViewModel
        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        // קבלת מזהה המשתמש הנוכחי באמצעות FirebaseAuth
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            // צפייה ב-LiveData שמחזיר את הפוסטים של המשתמש
            postViewModel.getPostsByUser(currentUserId).observe(viewLifecycleOwner) { posts ->
                postsAdapter.submitList(posts)
            }
        } else {
            // במידה והמשתמש אינו מחובר – ניתן להציג הודעה מתאימה או ניווט למסך ההתחברות
        }
    }
}
