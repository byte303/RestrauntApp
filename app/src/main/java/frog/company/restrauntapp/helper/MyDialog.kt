package frog.company.restrauntapp.helper

import android.app.AlertDialog
import android.content.Context
import frog.company.restrauntapp.R
import frog.company.restrauntapp.inter.IListenerDialog

class MyDialog(private val context : Context) {

    fun onExitUser(listenerDialog : IListenerDialog){
        val deleteDialog = AlertDialog.Builder(context, R.style.MyDialogTheme)

        deleteDialog
            .setTitle("Выход")
            .setMessage("Вы действительно хотите выйти из аккаунта?")
            .setCancelable(false)

        val del = deleteDialog.create()

        deleteDialog.setNegativeButton("Нет") { _, _ ->
            del.dismiss()
            listenerDialog.onResultDialog(false, 1)
        }

        deleteDialog.setPositiveButton("Да") { _, _ ->
            del.dismiss()
            listenerDialog.onResultDialog(true, 1)
        }
        deleteDialog.show()
    }
}