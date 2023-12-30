package net.kteam.pixelarthunter

class POIListSingleton private constructor(){
    private val poiList = arrayListOf<POI>()

    companion object{
        private var instance: POIListSingleton? = null

        fun getInstance(): POIListSingleton{
            if(instance == null){
                instance = POIListSingleton()
            }
            return instance as POIListSingleton
        }
    }

    fun getPoiList(): ArrayList<POI>{
        return poiList
    }
}