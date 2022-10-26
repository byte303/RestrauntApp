package frog.company.restrauntapp.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


class MyDate {
    @SuppressLint("SimpleDateFormat")
    fun onDateFormat(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")

        return sdf.format(Date())
    }

    fun onOnlyDateFormat(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy")

        return sdf.format(Date())
    }

    fun onOnlyTimeFormat(): String {
        val sdf = SimpleDateFormat("HH:mm")

        return sdf.format(Date())
    }
}