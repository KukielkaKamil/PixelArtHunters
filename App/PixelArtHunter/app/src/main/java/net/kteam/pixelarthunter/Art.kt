package net.kteam.pixelarthunter

class Art(
    var id: Int,
    var size: Int,
    var image: Array<Array<String>>,
    var owners: Array<User>,
    var poi: Poi
) {

    companion object {
        fun binStringToDecArray(blob: String, size: Int): Array<Array<String>> {
            var pixelCount = size * size
            val paddedBinaryString = blob.padEnd(pixelCount * 12, '0').substring(0, pixelCount * 12)
            // Initialize the 2D array
            val hexArray = Array(size) { Array(size) { "" } }

            // Iterate over the binary string and fill the 2D array
            for ((index, i) in (0 until pixelCount * 12 step 12).withIndex()) {
                val binarySegment = paddedBinaryString.substring(i, i + 12)
                val decimalValue = Integer.parseInt(binarySegment, 2)
                val hexValue = String.format("%06X", decimalValue)

                hexArray[index / size][index % size] = hexValue
            }

            return hexArray
        }
    }

    fun decArrayToBinString(): String {
        val binaryStringBuilder = StringBuilder()

        // Iterate over the 2D array and convert each hex value to binary
        for (row in image) {
            for (hexValue in row) {
                val decimalValue = Integer.parseInt(hexValue, 16)
                val binaryString = Integer.toBinaryString(decimalValue).padStart(12, '0')
                binaryStringBuilder.append(binaryString)
            }
        }

        return binaryStringBuilder.toString()
    }
}