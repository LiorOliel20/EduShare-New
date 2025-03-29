package com.example.edushare_new.ui.inspiration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.edushare_new.databinding.FragmentInspirationBinding
import com.example.edushare_new.viewmodel.InspirationViewModel

class InspirationFragment : Fragment() {

    private lateinit var binding: FragmentInspirationBinding
    private lateinit var viewModel: InspirationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInspirationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[InspirationViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.quote.observe(viewLifecycleOwner) { quote ->
            binding.tvQuoteText.text = "\"${quote.q}\""
            binding.tvAuthor.text = "- ${quote.a}"
        }

        binding.btnRefresh.setOnClickListener {
            viewModel.fetchRandomQuote()
        }

        viewModel.fetchRandomQuote()
    }
}