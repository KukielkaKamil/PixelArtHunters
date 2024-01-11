package net.kteam.pixelarthunter

import android.content.Context

class PoiList constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: PoiList? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PoiList(context).also {
                    INSTANCE = it
                }
            }
    }

    var poiList = ArrayList<Poi>()

    fun getList() : ArrayList<Poi>{
        return poiList;
    }
}