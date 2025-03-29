package com.example.edushare_new.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edushare_new.databinding.FragmentSearchBinding
import com.example.edushare_new.ui.home.PostsAdapter
import com.example.edushare_new.viewmodel.PostViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var adapter: PostsAdapter

    private val categories = listOf("All", "Math", "Science", "Languages", "History")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategories.adapter = spinnerAdapter

        // Initialize the adapter with a click listener for navigating to post details
        adapter = PostsAdapter { postId ->
            val action = SearchFragmentDirections.actionSearchFragmentToPostDetailFragment(postId)
            findNavController().navigate(action)
        }

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }

        viewModel.posts.observe(viewLifecycleOwner) { allPosts ->
            binding.etSearch.setOnEditorActionListener { _, _, _ ->
                val query = binding.etSearch.text.toString().trim()
                val category = binding.spinnerCategories.selectedItem.toString()
                val filtered = allPosts.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }.filter {
                    category == "All" || it.category.equals(category, ignoreCase = true)
                }
                adapter.submitList(filtered)
                true
            }
        }
    }
}
