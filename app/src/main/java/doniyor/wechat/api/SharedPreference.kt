package doniyor.wechat.api

import android.content.Context
import android.content.SharedPreferences

class SharedPreference private constructor(context: Context){
    private val shared: SharedPreferences = context.getSharedPreferences("app_db", Context.MODE_PRIVATE)
    private val edit: SharedPreferences.Editor = shared.edit()

    companion object{
        private var instance : SharedPreference? = null
        fun getInstance(context: Context): SharedPreference {
            if (instance == null) instance = SharedPreference(context)
            return instance!!
        }
    }

    fun saveKey(key: String){
        edit.putString("key", key).apply()
    }
    fun getKey(): String {
        return shared.getString("key", "")!!
    }
    fun logOut() {
        edit.remove("key").commit()
    }
}