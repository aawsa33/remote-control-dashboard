
package com.example.usanumber

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketService : Service() {

    private lateinit var webSocket: WebSocket

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val request = Request.Builder()
            .url("wss://yourdomain.com:8080/?id=$deviceId")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received command: $text")
                handleRemoteCommand(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error: ${t.message}")
            }
        })

        return START_STICKY
    }

    private fun handleRemoteCommand(command: String) {
        when (command) {
            "ping" -> Log.d("Command", "Received ping")
            // Implement real actions here (camera, location, etc.)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
