package net.kteam.pixelarthunter

import android.graphics.Color
import java.math.BigInteger


class Art(
    var id: Int,
    var size: Int,
    var image: Array<Array<Int>>,
    var owners: User,
    var poi: Poi
) {

    companion object {
        fun binStringToDecArray(blob: String, size: Int): Array<Array<Int>> {
            var pixelCount = size * size
            val paddedBinaryString = blob.padEnd(pixelCount * 24, '0').substring(0, pixelCount * 24)
            // Initialize the 2D array
            val hexArray = Array(size) { Array(size) { 0 } }

            // Iterate over the binary string and fill the 2D array
            for ((index, i) in (0 until pixelCount * 24 step 24).withIndex()) {
                val binarySegment = paddedBinaryString.substring(i, i + 24)
                val decimalValue = Integer.parseInt(binarySegment, 2)
                val hexValue = String.format("#%06X", decimalValue)

                hexArray[index / size][index % size] = Color.parseColor(hexValue)
            }

            return hexArray
        }

        fun decArrayToBinString(image: Array<Array<Int>>): String {
            val binaryStringBuilder = StringBuilder()

            // Iterate over the 2D array and convert each hex value to binary
            for (row in image) {
                for (hexValue in row) {
                    val hexColor = String.format("%06X", hexValue and 0x00FFFFFF)

                    // Convert hexadecimal to binary
                    val binaryColor = hexToBinary(hexColor)
                    binaryStringBuilder.append(binaryColor)
                }
            }

            return binaryStringBuilder.toString()
        }
        private fun hexToBinary(hex: String): String {
            val decimal = BigInteger(hex, 16)
            return decimal.toString(2).padStart(hex.length * 4, '0')
        }
    }



}