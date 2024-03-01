package com.bqmz001.uploadeasy.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.Locale


object OpenFileUtil {
    fun openFile(context: Context, filePath: String?): Intent{
        val file = File(filePath)
        if (!file.exists()) {
            throw ActivityNotFoundException("没有打开该文件的方法")
        }
        /* 取得扩展名 */
        val end = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length).lowercase(
            Locale.getDefault()
        )

        return when(end){
            "m4a","mp3","mid","xmf","ogg","wav","aac","flac","wma","ape"->{
                getAudioFileIntent(context,filePath)
            }
            "3gp","mp4","mov","flv","avi","webm","mkv"->{
                getVideoFileIntent(context,filePath)
            }
            "jpg","jpeg","png","gif","bmp","tiff"->{
                getImageFileIntent(context,filePath)
            }
            "apk"->{
                getApkFileIntent(context,filePath)
            }
            "ppt","pptx"->{
                getPptFileIntent(context,filePath)
            }
            "xls","xlsx"->{
                getExcelFileIntent(context,filePath)
            }
            "doc","docx","wps","ofd"->{
                getWordFileIntent(context,filePath)
            }
            "pdf"->{
                getPdfFileIntent(context,filePath)
            }
            "chm"->{
                getChmFileIntent(context,filePath)
            }
            "txt"->{
                getTextFileIntent(context,filePath)
            }
            else->{
                getAllIntent(context,filePath)

            }
        }

    }

    // Android获取一个用于打开APK文件的intent
    fun getAllIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "*/*")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开APK文件的intent
    fun getApkFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开VIDEO文件的intent
    fun getVideoFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "video/*")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开AUDIO文件的intent
    fun getAudioFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "audio/*")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开Html文件的intent
    fun getHtmlFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider")
            .scheme("content").encodedPath(param).build()

        intent.setDataAndType(uri, "text/html")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开图片文件的intent
    fun getImageFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val uri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开PPT文件的intent
    fun getPptFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开Excel文件的intent
    fun getExcelFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开Word文件的intent
    fun getWordFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "application/msword")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开CHM文件的intent
    fun getChmFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "application/x-chm")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }

    // Android获取一个用于打开文本文件的intent
    fun getTextFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "text/plain")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )

        return intent
    }

    // Android获取一个用于打开PDF文件的intent
    fun getPdfFileIntent(context: Context,param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri =
            FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.fileprovider", File(param));
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        return intent
    }
}