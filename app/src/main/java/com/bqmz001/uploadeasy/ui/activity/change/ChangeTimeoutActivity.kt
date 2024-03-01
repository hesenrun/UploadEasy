package com.bqmz001.uploadeasy.ui.activity.change

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bqmz001.uploadeasy.R
import com.bqmz001.uploadeasy.base.BaseActivity
import com.bqmz001.uploadeasy.databinding.ActivityChangeTimeoutBinding
import com.orhanobut.hawk.Hawk

class ChangeTimeoutActivity : BaseActivity<ActivityChangeTimeoutBinding>() {
    override fun initEmptyData() {

    }

    override fun initView() {
        setFinishOnTouchOutside(false)
        val timeout = Hawk.get<Int>("timeout", 10000)
        binding.etTimeout.setText((timeout/1000).toString())
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnOk.setOnClickListener {
            if (binding.etTimeout.text.toString().toIntOrNull() != null) {
                val newTimeout = binding.etTimeout.text.toString().toInt()
                if (newTimeout<10){
                    Toast.makeText(this@ChangeTimeoutActivity,"超时时间最短10s",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }else if (newTimeout>600){
                    Toast.makeText(this@ChangeTimeoutActivity,"写的有点多啦！",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }else{
                    Hawk.put("timeout",newTimeout*1000)
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    override fun bindData() {

    }

}