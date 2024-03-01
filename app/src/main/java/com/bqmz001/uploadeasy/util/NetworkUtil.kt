package com.bqmz001.uploadeasy.util

import java.net.NetworkInterface
import java.net.SocketException

object NetworkUtil {
    fun getWLANorAPAddress(): String {
        var hotspotAddress = ""
        var wlanAddress = ""
        var rndisAddress = ""
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()

            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                if (networkInterface.displayName.contains("wlan") || networkInterface.displayName.contains(
                        "rndis"
                    ) || networkInterface.displayName.contains("usb")
                ) {
                    // getInterfaceAddresses
                    val interfaceAddressList = networkInterface.interfaceAddresses
                    for (interfaceAddress in interfaceAddressList) {
                        val inetAddress = interfaceAddress.address
                        if (!inetAddress.isLinkLocalAddress) {
                            if (networkInterface.displayName.contains("wlan0")) {
                                wlanAddress = inetAddress.toString().substring(1)
                            } else if (networkInterface.displayName.contains("wlan1")) {
                                hotspotAddress = inetAddress.toString().substring(1)
                            } else if (networkInterface.displayName.contains("rndis") || networkInterface.displayName.contains(
                                    "usb"
                                )
                            ) {
                                rndisAddress = inetAddress.toString().substring(1)

                            }
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return if (wlanAddress.isNotEmpty()) {
            wlanAddress
        } else if (hotspotAddress.isNotEmpty()) {
            hotspotAddress
        } else if (rndisAddress.isNotEmpty()) {
            rndisAddress
        } else {
            "127.0.0.1"
        }
    }
}