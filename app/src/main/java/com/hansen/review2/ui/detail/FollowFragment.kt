package com.hansen.review2.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hansen.review2.data.adapter.FollowerAdapter
import com.hansen.review2.data.adapter.FollowingAdapter
import com.hansen.review2.data.response.FollowersResponseItem
import com.hansen.review2.data.response.FollowingResponseItem
import com.hansen.review2.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val mainViewModel by activityViewModels<DetailViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)
//        tvLabel.text = getString(R.string.content_tab_text, position)
         if (position == 1){
             username?.let { mainViewModel.findFollower(it) }
             mainViewModel.listFollower.observe(viewLifecycleOwner) { listFollower ->
                 setFollowerData(listFollower)
             }
             mainViewModel.isLoading.observe(viewLifecycleOwner) {
                 showLoading(it)
             }
             val layoutManager = LinearLayoutManager(requireActivity())
             binding.rvFollow.layoutManager = layoutManager
             val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
             binding.rvFollow.addItemDecoration(itemDecoration)

         } else {
             username?.let { mainViewModel.findFollowing(it) }
             mainViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
                 setFollowingData(listFollowing)
             }
             mainViewModel.isLoading.observe(viewLifecycleOwner) {
                 showLoading(it)
             }
             val layoutManager = LinearLayoutManager(requireActivity())
             binding.rvFollow.layoutManager = layoutManager
             val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
             binding.rvFollow.addItemDecoration(itemDecoration)
        }
    }

    private fun setFollowerData(listFollower: List<FollowersResponseItem>?) {
        val adapter = FollowerAdapter()
        adapter.submitList(listFollower)
        binding.rvFollow.adapter = adapter
    }

    private fun setFollowingData(listFollowing: List<FollowingResponseItem>?) {
        val adapter = FollowingAdapter()
        adapter.submitList(listFollowing)
        binding.rvFollow.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}