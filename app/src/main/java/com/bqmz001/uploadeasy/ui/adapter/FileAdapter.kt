package com.bqmz001.uploadeasy.ui.adapter

import com.bqmz001.uploadeasy.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.io.File

class FileAdapter(list:MutableList<File>):BaseQuickAdapter<File,BaseViewHolder>(R.layout.item_file,list) {
    override fun convert(holder: BaseViewHolder, item: File) {
        holder.setText(R.id.tv_file_name,item.name)
    }
}