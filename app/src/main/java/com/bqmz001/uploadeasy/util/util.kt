package com.bqmz001.uploadeasy.util

import android.app.Application
import android.content.Intent
import android.content.res.Resources

fun viewConvertDp2Px(i: Int): Float {
    val scale: Float = Resources.getSystem().displayMetrics.density
    return (i * scale + 0.5f)
}

fun restartApp(app: Application) {
    val intent = app.packageManager.getLaunchIntentForPackage(app.packageName);
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    val componentName = intent.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    app.startActivity(mainIntent);
    //杀掉以前进程
    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(0)

}
