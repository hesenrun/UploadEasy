package com.bqmz001.uploadeasy.service

import android.content.Context
import android.os.Environment
import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class MyFileServer(private val context: Context, port: Int) : NanoHTTPD(port) {
    override fun serve(session: IHTTPSession?): Response {
        if (session != null) {
            printSessionInfo(session)
            val ct = ContentType(session.headers["content-type"]).tryUTF8()
            session.headers["content-type"] = ct.contentTypeHeader
            if (!session.remoteIpAddress.startsWith("192")) {
                return return405()
            }

            when (session.uri) {
                "/index" -> {
                    return returnIndexWebPage()
                }

                "/uploadFile" -> {
                    if (!session.method.equals(Method.POST)) {
                        return return405()
                    }
                    val body = mutableMapOf<String, String>()
                    try {
                        session.parseBody(body)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return return500()
                    }

                    val params = session.parameters
                    for (param in params) {
                        if (param.key.contains("file")) {
                            val tmpPath = body[param.key]
                            if(tmpPath.toString().equals("")){
                                return returnNoFileWebPage()
                            }
                            var fileName = param.value[0]
                            val tmpFile = File(tmpPath)
                            if (!checkDictionaryExist()) {
                                createDictionary()
                            }
                            val targetFile =
                                File(Environment.getExternalStorageDirectory().path + "/Download/HttpFileServer/${System.currentTimeMillis()}_${fileName}")
                            copyFile(tmpFile, targetFile)
                            return returnSuccessWebPage()
                        }
                    }
                }

                "/uploadFileByApp" -> {
                    if (!session.method.equals(Method.POST)) {
                        return return405()
                    }
                    val body = mutableMapOf<String, String>()
                    try {
                        session.parseBody(body)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return return500()
                    }

                    val params = session.parameters
                    for (param in params) {
                        if (param.key.contains("file")) {
                            val tmpPath = body[param.key]
                            var fileName = param.value[0]
                            val tmpFile = File(tmpPath)
                            if (!checkDictionaryExist()) {
                                createDictionary()
                            }
                            val targetFile =
                                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/HttpFileServer/${System.currentTimeMillis()}_${fileName}")
                            copyFile(tmpFile, targetFile)
                            return return200()
                        }
                    }
                }
            }

        }
        return return404()
    }

    fun return404(): Response =
        NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "404 Not Found")

    fun return405(): Response =
        NanoHTTPD.newFixedLengthResponse(
            Response.Status.METHOD_NOT_ALLOWED,
            MIME_PLAINTEXT,
            "405 Method Not Allowed"
        )

    fun return500(): Response =
        NanoHTTPD.newFixedLengthResponse(
            Response.Status.INTERNAL_ERROR,
            MIME_PLAINTEXT,
            "500 Internal Server Error"
        )

    fun return200(): Response = NanoHTTPD.newFixedLengthResponse("Success!")

    fun returnIndexWebPage(): Response = NanoHTTPD.newFixedLengthResponse(
        Response.Status.OK, MIME_HTML, "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<header>\n" +
                "  <title>上传文件</title>\n" +
                "</header>\n" +
                "<body>\n" +
                "  <h1 style=\"text-align:center;margin-top: 5vw; font-size: 7vw;\">一个简陋的上传文件页面</h1>\n" +
                "  <form action=\"/uploadFile\" method=\"POST\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\" style=\"text-align:center;margin-top: 15vw;\">\n" +
                "    <input type=\"file\" name=\"file\" style=\"font-size: 3vw;\"/>\n" +
                "    <input type=\"submit\" value=\"上传文件\" style=\"font-size: 3vw;\"/>\n" +
                "  </form>\n" +
                "  <p style=\"text-align:center;margin-top: 15vw;font-size: 3vw;\">食用方法：点左边的选择文件，选好了点右边上传就行</p>\n" +
                "  <p style=\"text-align:center;font-size: 3vw;\">别问，问就是能传上去</p>\n" +
                "  <p style=\"text-align:center;font-size: 3vw;\">play by bqmz001</p>\n" +
                "</body>\n" +
                "</html>"
    )

    fun returnSuccessWebPage(): Response = NanoHTTPD.newFixedLengthResponse(
        Response.Status.OK, MIME_HTML, "<!DOCTYPE html>\n" +
                "<header>\n" +
                "  <title>传完了</title>\n" +
                "</header>\n" +
                "<body>\n" +
                "  <h1 style=\"text-align:center;margin-top: 5vw; font-size: 7vw;\">好了，传完了</h1>\n" +
                "  <p style=\"text-align:center;margin-top: 0px; font-size: 3vw;\">点击下方按钮返回上传文件页面</p>\n" +
                "  <div style=\"text-align:center;margin-top: 10vw;\"><button style=\"text-align:center;font-size: 5vw;\"  onclick=\"window.location.href='/index'\">返回页面</button></div>\n" +
                "  <p style=\"text-align:center;font-size: 3vw;margin-top: 10vw;\">play by bqmz001</p>\n" +
                "</body> "
    )

    fun returnNoFileWebPage(): Response = NanoHTTPD.newFixedLengthResponse(
        Response.Status.OK, MIME_HTML, "<!DOCTYPE html>\n" +
                "<header>\n" +
                "  <title>你传的文件呢？</title>\n" +
                "</header>\n" +
                "<body>\n" +
                "  <h1 style=\"text-align:center;margin-top: 5vw; font-size: 7vw;\">你传的文件呢？</h1>\n" +
                "  <p style=\"text-align:center;margin-top: 0px; font-size: 3vw;\">点击下方按钮返回上传文件页面</p>\n" +
                "  <div style=\"text-align:center;margin-top: 10vw;\"><button style=\"text-align:center;font-size: 5vw;\"  onclick=\"window.location.href='/index'\">返回页面</button></div>\n" +
                "  <p style=\"text-align:center;font-size: 3vw;margin-top: 10vw;\">play by bqmz001</p>\n" +
                "</body> "
    )

    fun checkDictionaryExist(): Boolean {
        val dictPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/HttpFileServer"
        val file = File(dictPath)
        return file.exists() && file.isDirectory
    }

    fun createDictionary() {
        val dictPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/HttpFileServer"
        val file = File(dictPath)
        if (file.exists() && !file.isDirectory) {
            file.delete()
            val newFile = File(dictPath)
            newFile.mkdir()
        } else if (!file.exists()) {
            file.mkdir()
        }
    }

    fun printSessionInfo(session: IHTTPSession) {
        Log.d("httpd", "---------------------------------------------------------")
        Log.d("httpd", "from ${session.remoteIpAddress} - ${session.method} - ${session.uri}")
        for (param in session.parameters) {
            val builder = StringBuilder()
            builder.append("K:${param.key} - V:")
            for (pv in param.value) {
                builder.append("${pv},")
            }
            Log.d("httpd", builder.toString())
        }
        Log.d("httpd", "---------------------------------------------------------")
    }

    fun copyFile(file: File, targetfile: File?) {
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        if (!file.exists()) {
            System.err.println("File not exists!")
            return
        }
        try {
            fis = FileInputStream(file)
            fos = FileOutputStream(targetfile)
            val buffer = ByteArray(1024)
            var len = 0
            while (fis.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
                fos.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fis?.close()
                fos?.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }
}