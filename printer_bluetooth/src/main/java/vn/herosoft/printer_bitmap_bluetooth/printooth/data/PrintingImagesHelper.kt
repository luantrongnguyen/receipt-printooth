package vn.herosoft.printer_bitmap_bluetooth.printooth.data

import android.graphics.Bitmap

interface PrintingImagesHelper {
    fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray
}