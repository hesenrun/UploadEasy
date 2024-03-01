package com.bqmz001.uploadeasy.ui.activity.change

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.bqmz001.uploadeasy.R
import com.bqmz001.uploadeasy.base.BaseActivity
import com.bqmz001.uploadeasy.databinding.ActivityChangePortBinding
import com.orhanobut.hawk.Hawk

class ChangePortActivity : BaseActivity<ActivityChangePortBinding>() {
    override fun initEmptyData() {

    }

    override fun initView() {
        setFinishOnTouchOutside(false)
        val port = Hawk.get("port", 8888)
        binding.etPort.setText(port.toString())
        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish() }
        binding.btnOk.setOnClickListener {
            if (binding.etPort.text.toString().toIntOrNull()!=null){
                when(binding.etPort.text.toString().toInt()){
                    0,22,80,443,8080,5555,5556->{
                        Toast.makeText(this@ChangePortActivity,"请避开常用的端口，否则将会引起冲突",Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else->{
                        Hawk.put("port",binding.etPort.text.toString().toInt())
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        }
    }

    override fun bindData() {

    }

}