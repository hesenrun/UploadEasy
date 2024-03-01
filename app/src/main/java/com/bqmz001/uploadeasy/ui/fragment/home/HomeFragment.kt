package com.bqmz001.uploadeasy.ui.fragment.home

import android.graphics.Bitmap
import android.graphics.Color
import com.bqmz001.uploadeasy.base.BaseFragment
import com.bqmz001.uploadeasy.databinding.FragmentHomeBinding
import com.bqmz001.uploadeasy.util.NetworkUtil
import com.bqmz001.uploadeasy.util.restartApp
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.orhanobut.hawk.Hawk
import java.util.Hashtable

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var httpAddress =
        "http://${NetworkUtil.getWLANorAPAddress()}:${Hawk.get("port", 8888)}/index"

    override fun initEmptyData() {

    }

    override fun initView() {
        loadQr(httpAddress)
        binding.tvHint.setText("传输文件过程中请保持APP前台运行\nPC端请访问${httpAddress}")

        binding.btnRefreshQr.setOnClickListener {
            httpAddress =
                "http://${NetworkUtil.getWLANorAPAddress()}:${Hawk.get("port", 8888)}/index"
            loadQr(httpAddress)
            binding.tvHint.setText("传输文件过程中请保持APP前台运行\nPC端请访问${httpAddress}")

        }

        binding.btnRestartApp.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("提示")
                .setMessage("你确定要重启服务吗？这将会使现有的上传任务中断")
                .setPositiveButton("确定") { dialog, which ->
                    restartApp(requireActivity().application)
                }
                .setNegativeButton("取消"){dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    override fun bindData() {

    }

    fun loadQr(content: String) {
        val colorBlack = Color.BLACK
        val colorWhite = Color.WHITE
        val size = 240
        val hints = Hashtable<EncodeHintType, String?>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8" // 字符转码格式设置

        hints[EncodeHintType.ERROR_CORRECTION] = "H" // 容错级别设置 默认为L

        hints[EncodeHintType.MARGIN] = "4" // 空白边距设置

        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)

        val pixels = IntArray(size * size)
        for (y in 0 until size) {
            for (x in 0 until size) {
                if (bitMatrix[x, y]) { // 黑色色块像素设置
                    pixels[y * size + x] = colorBlack
                } else { // 白色色块像素设置
                    pixels[y * size + x] = colorWhite
                }
            }
        }

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
        Glide.with(this).load(bitmap).into(binding.ivQr)
    }
}