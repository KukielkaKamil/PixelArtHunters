package net.kteam.pixelarthunter

class POI(
    val id: Int,
    val name: String,
    var longitude: Double,
    var latitude: Double,
    var modifier: Double
) {
    // Custom constructor
    constructor(id: Int, name: String, longitude: Double, latitude: Double) : this(id, name, longitude, latitude, 1.0)
}
