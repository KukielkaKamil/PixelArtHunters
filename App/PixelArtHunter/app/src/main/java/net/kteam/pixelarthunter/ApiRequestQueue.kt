package net.kteam.pixelarthunter

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class ApiRequestQueue constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: ApiRequestQueue? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiRequestQueue(context).also {
                    INSTANCE = it
                }
            }
    }

    private var userToken: String? = null

    fun getToken(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("Token", "") ?: ""
    }
    fun removeToken(context: Context){
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

// Step 2: Remove Data
        editor.remove("Token")

// Step 3: Commit Changes
        editor.apply()
    }
    fun setToken(context: Context, token: String){
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Token",token)
        editor.apply()
    }

    var user: User = User()

    var poiList = ArrayList<Poi>()

    fun getList() : ArrayList<Poi>{
        return poiList;
    }


    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}