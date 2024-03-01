package com.bqmz001.uploadeasy.ui.activity.guide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bqmz001.uploadeasy.R
import com.bqmz001.uploadeasy.base.BaseActivity
import com.bqmz001.uploadeasy.databinding.ActivityGuideBinding

class GuideActivity : BaseActivity<ActivityGuideBinding>() {
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