package com.bqmz001.uploadeasy

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bqmz001.uploadeasy.base.BaseActivity
import com.bqmz001.uploadeasy.databinding.ActivityMainBinding
import com.bqmz001.uploadeasy.service.HttpdService
import com.bqmz001.uploadeasy.ui.activity.change.ChangePortActivity
import com.bqmz001.uploadeasy.util.restartApp
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigationrail.NavigationRailView
import com.permissionx.guolindev.PermissionX
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity<ActivityMainBinding>() {
    val requestDataLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                restartApp(application)
            } else if (result.resultCode == RESULT_CANCELED) {
                startFailHint()
            }
        }

    @SuppressLint("NewApi")
    val permissionRequestDataLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(this@MainActivity, "授权成功", Toast.LENGTH_SHORT).show()
            }else{
                MaterialAlertDialogBuilder(this)
                    .setCancelable(false)
                    .setTitle("权限提醒")
                    .setMessage("没有获取到授权会导致体验不佳，确定继续？")
                    .setPositiveButton("爷不玩了") { dialog, which ->
                        finish()
                    }
                    .setNegativeButton("就图一乐") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }


    override fun initEmptyData() {

    }

    override fun initView() {
        val navView: NavigationBarView = binding.bottomNavigation as NavigationBarView
//        navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        if (checkStoragePermission()) {
            startService(Intent(this@MainActivity, HttpdService::class.java))
        } else {
            MaterialAlertDialogBuilder(this)
                .setTitle("权限提醒")
                .setMessage("貌似没有获取存储权限，但这个APP没有存储权限就没法保存上传的文件，还得大哥们高抬贵手点个授权啊")
                .setCancelable(false)
                .setPositiveButton("确定授权") { dialog, which ->
                    requestStoragePermission()
                }
                .setNegativeButton("不授权，我就先看看") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
        EventBus.getDefault().register(this)
    }

    override fun bindData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageArrived(text: String) {
        when (text) {
            "START_FAIL" -> {
                startFailHint()
            }

            "UNKNOWN_ERROR" -> {
                unknownHint()
            }

            else -> {}
        }
    }

    fun unknownHint() {
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle("启动失败")
            .setMessage("未知错误，摇人解决吧")
            .setPositiveButton("确定") { dialog, which ->
                finish()
            }
            .show()
    }

    fun startFailHint() {
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle("启动失败")
            .setMessage("貌似端口冲突了，换个端口试试？")
            .setPositiveButton("确定") { dialog, which ->
                requestDataLauncher.launch(
                    Intent(
                        this@MainActivity,
                        ChangePortActivity::class.java
                    )
                )
            }
            .setNegativeButton("离开") { dialog, which ->
                finish()
            }
            .show()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        if (checkStoragePermission()) {
            stopService(Intent(this@MainActivity, HttpdService::class.java))
        }
        super.onDestroy()
    }

    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.isGranted(
                this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ) || Environment.isExternalStorageManager()
        } else {
            PermissionX.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val newIntent =  Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            newIntent.setData(Uri.parse("package:" + packageName));
            permissionRequestDataLauncher.launch(newIntent)
        } else {
            PermissionX.init(this)
                .permissions(getStoragePermission())
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
                        startService(Intent(this@MainActivity, HttpdService::class.java))
                    } else {
                        MaterialAlertDialogBuilder(this)
                            .setCancelable(false)
                            .setTitle("权限提醒")
                            .setMessage("没有获取到授权会导致体验不佳，确定继续？")
                            .setPositiveButton("爷不玩了") { dialog, which ->
                                finish()
                            }
                            .setNegativeButton("就图一乐") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()


                    }
                }
        }
    }


}