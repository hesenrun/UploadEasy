package com.bqmz001.uploadeasy.ui.fragment.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bqmz001.uploadeasy.base.BaseFragment
import com.bqmz001.uploadeasy.databinding.FragmentSettingsBinding
import com.bqmz001.uploadeasy.ui.activity.about.AboutActivity
import com.bqmz001.uploadeasy.ui.activity.change.ChangePortActivity
import com.bqmz001.uploadeasy.ui.activity.change.ChangeTimeoutActivity
import com.bqmz001.uploadeasy.ui.activity.guide.GuideActivity
import com.bqmz001.uploadeasy.ui.activity.v50.V50Activity
import com.orhanobut.hawk.Hawk
import com.permissionx.guolindev.PermissionX

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val requestDataLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(requireContext(), "修改成功，重启服务生效", Toast.LENGTH_SHORT).show()
                refreshView()
            }
        }

    @SuppressLint("NewApi")
    private val permissionRequestDataLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (Environment.isExternalStorageManager()){
            Toast.makeText(requireContext(), "授权成功", Toast.LENGTH_SHORT).show()
        }
    }

    override fun initEmptyData() {
    }

    override fun initView() {
        refreshView()
        binding.btnPort.setOnClickListener {
            requestDataLauncher.launch(Intent(requireContext(), ChangePortActivity::class.java))
        }
        binding.btnTimeout.setOnClickListener {
            requestDataLauncher.launch(Intent(requireContext(), ChangeTimeoutActivity::class.java))
        }
        binding.btnPermission.setOnClickListener {
            if (checkStoragePermission()) {
                Toast.makeText(requireContext(),"APP已取得该权限",Toast.LENGTH_SHORT).show()
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val newIntent =  Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    newIntent.setData(Uri.parse("package:" + requireContext().packageName));
                    permissionRequestDataLauncher.launch(newIntent)
                }else {
                    PermissionX.init(this)
                        .permissions(getStoragePermission())
                        .request { allGranted, grantedList, deniedList ->
                            if (allGranted) {
                                Toast.makeText(requireContext(), "授权成功", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(requireContext(), "未获得授权", Toast.LENGTH_SHORT)
                                    .show()

                            }
                        }
                }
            }
        }
        binding.btnGuide.setOnClickListener {
            startActivity(Intent(requireContext(),GuideActivity::class.java))
        }
        binding.btnAbout.setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }
        binding.btnGithub.setOnClickListener {
            val uri = Uri.parse("https://github.com/hesenrun/UploadEasy")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        binding.btnV50.setOnClickListener {
            startActivity(Intent(requireContext(), V50Activity::class.java))

        }
    }

    override fun bindData() {

    }


    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.isGranted(requireContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        } else {
            PermissionX.isGranted(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun getStoragePermission(): MutableList<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableListOf<String>(
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            )
        } else {
            mutableListOf<String>(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    fun refreshView() {
        val port = Hawk.get<Int>("port", 8888)
        val timeout = Hawk.get<Int>("timeout", 10000)
        binding.btnPort.setText("端口号:${port}")
        binding.btnTimeout.setText("超时:${timeout / 1000}s")
    }


}