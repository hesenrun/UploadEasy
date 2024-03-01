package com.bqmz001.uploadeasy.ui.fragment.files

import android.content.ActivityNotFoundException
import android.graphics.Rect
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bqmz001.uploadeasy.R
import com.bqmz001.uploadeasy.base.BaseFragment
import com.bqmz001.uploadeasy.databinding.FragmentFilesBinding
import com.bqmz001.uploadeasy.ui.adapter.FileAdapter
import com.bqmz001.uploadeasy.util.OpenFileUtil
import com.bqmz001.uploadeasy.util.viewConvertDp2Px
import java.io.File
import java.util.Arrays

class FilesFragment : BaseFragment<FragmentFilesBinding>() {
    var list = mutableListOf<File>()
    lateinit var adapter: FileAdapter
    var screenWidth=0
    override fun initEmptyData() {
        list.clear()
        screenWidth=resources.configuration.screenWidthDp
    }

    override fun initView() {
        val dictionary =
            File(Environment.getExternalStorageDirectory().path + "/Download/HttpFileServer")
        val files = dictionary.listFiles()
        if (files != null) {
            Arrays.sort(files) { p0, p1 ->
                val lastModified1: Long = p0.lastModified()
                val lastModified2: Long = p1.lastModified()
                return@sort lastModified1.compareTo(lastModified2)
            }
            files.reverse()
            list.clear()
            list.addAll(files.toMutableList())
        }
        binding.rvList.layoutManager = GridLayoutManager(requireContext(), if (screenWidth<900) 2 else 4)
        adapter = FileAdapter(list)
        adapter.setEmptyView(layoutInflater.inflate(R.layout.empty_file,null,false))
        adapter.setOnItemClickListener { adapter, view, position ->
            try {
                val intent = OpenFileUtil.openFile(requireContext(), list[position].path)
                startActivity(intent)
            } catch (e: Exception) {
                if (e is ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "没有打开该文件的方法", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "文件打开错误", Toast.LENGTH_SHORT).show()
                }
            }

        }
        binding.rvList.adapter = adapter
        binding.rvList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                itemPosition: Int,
                parent: RecyclerView
            ) {
                if (screenWidth<900) {
                    if (itemPosition % 2 == 0) {
                        outRect.set(
                            viewConvertDp2Px(16).toInt(),
                            viewConvertDp2Px(8).toInt(),
                            viewConvertDp2Px(8).toInt(),
                            viewConvertDp2Px(8).toInt()
                        )
                    } else {
                        outRect.set(
                            viewConvertDp2Px(8).toInt(),
                            8,
                            viewConvertDp2Px(16).toInt(),
                            viewConvertDp2Px(8).toInt()
                        )
                    }
                }else{
                    if (itemPosition % 4 == 0) {
                        outRect.set(
                            viewConvertDp2Px(16).toInt(),
                            viewConvertDp2Px(8).toInt(),
                            viewConvertDp2Px(8).toInt(),
                            viewConvertDp2Px(8).toInt()
                        )
                    } else if(itemPosition % 4 == 3){
                        outRect.set(
                            viewConvertDp2Px(8).toInt(),
                            8,
                            viewConvertDp2Px(16).toInt(),
                            viewConvertDp2Px(8).toInt()
                        )
                    }else{
                        outRect.set(
                            viewConvertDp2Px(8).toInt(),
                            8,
                            viewConvertDp2Px(8).toInt(),
                            viewConvertDp2Px(8).toInt()
                        )
                    }
                }
            }
        })
        binding.refresh.setOnRefreshListener {
            val dictionary =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/HttpFileServer")
            val files = dictionary.listFiles()
            if (files != null) {
                Arrays.sort(files) { p0, p1 ->
                    val lastModified1: Long = p0.lastModified()
                    val lastModified2: Long = p1.lastModified()
                    return@sort lastModified1.compareTo(lastModified2)
                }
                files.reverse()
                list.clear()
                list.addAll(files.toMutableList())
            }
            adapter.notifyDataSetChanged()
            binding.refresh.isRefreshing = false

        }

    }

    override fun bindData() {
    }
}