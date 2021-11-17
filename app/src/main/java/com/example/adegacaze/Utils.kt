package com.example.adegacaze

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.example.adegacaze.model.UsuarioLogin
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun getTokenUser(context: Context): String? {
    return getPreferences(context).getString("Token", "");
}

fun getUserName(context: Context): String? {
    return getPreferences(context).getString("UserName", "");
}

fun getUserId(context: Context): Int? {
    return getPreferences(context).getInt("UserId", 0);
}

fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("prefUser", Context.MODE_PRIVATE);
}

fun setUserPreferences(context: Context, usuario: UsuarioLogin) {
    val editorUser = getPreferences(context).edit()

    editorUser.putString("Token", usuario.resp.token);
    editorUser.putString("UserName", usuario.resp.user.name);
    editorUser.putInt("UserId", usuario.resp.user.id);

    editorUser.commit();
}

fun removeUserPreferences(context: Context) {
    val pref = context.getSharedPreferences("prefUser", Context.MODE_PRIVATE);
    val edit = pref.edit();

    edit.remove("Token");
    edit.remove("UserName");
    edit.remove("UserId");

    edit.commit()
}

fun formatDate(
    date: String?,
    initDateFormat: String?,
    endDateFormat: String?
): String {
    val initDate = SimpleDateFormat(initDateFormat).parse(date)
    val formatter = SimpleDateFormat(endDateFormat)
    return formatter.format(initDate)
}

fun formatarDouble(number: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(number);
}


fun showSnack(view: View, titulo: String) {
    Snackbar.make(
        view,
        titulo,
        Snackbar.LENGTH_SHORT
    ).show();
}