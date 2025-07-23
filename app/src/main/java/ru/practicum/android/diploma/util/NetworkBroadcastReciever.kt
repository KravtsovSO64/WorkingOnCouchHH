package ru.practicum.android.diploma.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NetworkBroadcastReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.net.conn.CONNECTIVITY_CHANGE" && !CheckConnection.isConnected(context as Context)) {
                Toast.makeText(context, "Отсутствует подключение к интернету", Toast.LENGTH_LONG)
                    .show()
        }
    }
}
