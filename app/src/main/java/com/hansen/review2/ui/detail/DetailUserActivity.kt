package com.hansen.review2.ui.detail

import android.content.res.ColorStateList
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hansen.review2.R
import com.hansen.review2.data.adapter.SectionPagerAdapter
import com.hansen.review2.data.response.ItemsItem
import com.hansen.review2.databinding.ActivityDetailUserBinding
import com.hansen.review2.data.database.DbModule

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailViewModel>{
        DetailViewModel.Factory(DbModule(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<ItemsItem>("item")
        if (item != null) {
            viewModel.findDetailUser(item.login ?:"")
        } else {
            Log.e(TAG, "Username is null")
        }


        viewModel.detailUser.observe(this) { detailUserResponse ->
            binding.apply {
                tvName.text = detailUserResponse.name
                tvUsername.text = detailUserResponse.login
                tvFollowersValue.text = detailUserResponse.followers.toString()
                tvFollowingValue.text = detailUserResponse.following.toString()
            }

            Glide.with(this@DetailUserActivity)
                .load(detailUserResponse.avatarUrl)
                .into(binding.imgAvatar)

            val sectionAdapter = SectionPagerAdapter(this@DetailUserActivity, detailUserResponse.login.toString())
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionAdapter

            val tabs: TabLayout = findViewById(R.id.tabs)

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                val customTabView = layoutInflater.inflate(R.layout.custom_tab_item, null) as TextView
                customTabView.text = resources.getString(TAB_TITLES[position])
                val isNightMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                if (isNightMode){
                    customTabView.setTextColor(ContextCompat.getColor(this, R.color.white))
                }else{
                    customTabView.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
                }
                tab.customView = customTabView
            }.attach()
            supportActionBar?.elevation = 0f

        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.resultSuccessFavorite.observe(this){
            binding.favAdd.changeIconColor(R.color.red)
        }

        binding.favAdd.setOnClickListener{
            item?.let { result -> viewModel.setFavoriteUser(result) }
        }
        viewModel.findFavorite(item?.id ?:0){
            binding.favAdd.changeIconColor(R.color.red)
        }

        viewModel.resultDeleteFavorite.observe(this){
            binding.favAdd.changeIconColor(R.color.white)
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
    companion object {
        private const val TAG = "DetailUserActivity"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}
