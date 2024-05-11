package com.hansen.review2.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hansen.review2.data.adapter.ReviewAdapter
import com.hansen.review2.data.response.ItemsItem
import com.hansen.review2.databinding.ActivityFavoriteBinding
import com.hansen.review2.ui.detail.DetailUserActivity
import com.hansen.review2.data.database.DbModule

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private val viewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getUserFavorite().observe(this) { listUser ->
            setUserData(listUser)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavorite.addItemDecoration(itemDecoration)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserFavorite().observe(this) { listUser ->
            setUserData(listUser)
        }
    }
    private fun setUserData(userData: List<ItemsItem>) {
        val adapter = ReviewAdapter{item ->
            val intent = Intent(this, DetailUserActivity::class.java)
            intent.putExtra("item",item)
            startActivity(intent)
        }
        adapter.submitList(userData)
        binding.rvFavorite.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}