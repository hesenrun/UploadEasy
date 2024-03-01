package com.bqmz001.uploadeasy.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.orhanobut.hawk.Hawk
import org.greenrobot.eventbus.EventBus
import java.io.IOException

class HttpdService : Service() {
    lateinit var httpd: MyFileServer
    override fun onCreate() {
        super.onCreate()
        val port=Hawk.get<Int>("port",8888)
        val timeout=Hawk.get<Int>("timeout",10000)
        httpd= MyFileServer(this,port)
        try{
            httpd.start(timeout)
        }catch (e:Exception){
            e.printStackTrace()
            if(e is IOException){
                EventBus.getDefault().post("START_FAIL")
            }else{
                EventBus.getDefault().post("UNKNOWN_ERROR")
            }
        }
    }

    override fun onDestroy() {
        httpd.stop()
        super.onDestroy()
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}