package com.bqmz001.uploadeasy.ui.activity.about

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bqmz001.uploadeasy.R
import com.bqmz001.uploadeasy.base.BaseActivity
import com.bqmz001.uploadeasy.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity<ActivityAboutBinding>() {
    override fun initEmptyData() {

    }

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun bindData() {

    }

}