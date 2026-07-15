package com.rick.tcgscanner.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File

object ImageProcessor {

    fun compressForVision(file: File, maxWidth: Int = 1024, quality: Int = 85): ByteArray {
        val original = BitmapFactory.decodeFile(file.absolutePath)
            ?: throw IllegalStateException("Unable to decode image")

        return try {
            val scaled: Bitmap
            val shouldRecycleScaled: Boolean
            if (original.width > maxWidth) {
                val ratio = maxWidth.toFloat() / original.width
                scaled = Bitmap.createScaledBitmap(
                    original,
                    maxWidth,
                    (original.height * ratio).toInt(),
                    true
                )
                shouldRecycleScaled = true
            } else {
                scaled = original
                shouldRecycleScaled = false
            }

            val bytes = ByteArrayOutputStream().use { out ->
                scaled.compress(Bitmap.CompressFormat.JPEG, quality, out)
                out.toByteArray()
            }

            if (shouldRecycleScaled && !scaled.isRecycled) scaled.recycle()
            bytes
        } finally {
            if (!original.isRecycled) original.recycle()
        }
    }
}
