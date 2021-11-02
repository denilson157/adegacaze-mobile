package com.example.adegacaze

import android.content.Context
import android.content.SharedPreferences

fun getTokenUser(context: Context): String? {
    return getPreferences(context).getString("Token", "");
}

fun getUserName(context: Context): String? {
    return getPreferences(context).getString("UserName", "");
}

fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("prefUser", Context.MODE_PRIVATE);
}

fun removeUserPreferences(context: Context) {
    val pref = context.getSharedPreferences("prefUser", Context.MODE_PRIVATE);
    val edit = pref.edit()
    edit.remove("Token");
    edit.remove("UserName");
    edit.commit()
}