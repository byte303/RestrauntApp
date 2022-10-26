package frog.company.restrauntapp.database

import frog.company.restrauntapp.MainActivity
import frog.company.restrauntapp.MainCookActivity
import io.paperdb.Paper

object UtilsDB {
    // 192.168.31.47
    const val URL = "URL"
    var url = ""
    init {
        url = Paper.book().read(URL, "").toString()

    }

    var mainUrl = "http://$url/android/php/"

    const val USER = "USER"
    const val CASHE = "CASHE"
    const val PAPER_COUNT = "PAPER_COUNT"

    lateinit var MY_ACTIVITY : MainCookActivity
}