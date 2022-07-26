package com.jpb.scratchtappy.md3.utils

import android.os.StatFs
import com.jpb.scratchtappy.md3.utils.DiskUtils
import android.os.Environment

object DiskUtils {
    private const val MEGA_BYTE: Long = 1048576

    /**
     * Calculates total space on disk
     * @param external  If true will query external disk, otherwise will query internal disk.
     * @return Number of mega bytes on disk.
     */
    fun totalSpace(external: Boolean): Int {
        val statFs = getStats(external)
        val total = statFs.blockCount.toLong() * statFs.blockSize
            .toLong() / MEGA_BYTE
        return total.toInt()
    }

    /**
     * Calculates free space on disk
     * @param external  If true will query external disk, otherwise will query internal disk.
     * @return Number of free mega bytes on disk.
     */
    fun freeSpace(external: Boolean): Int {
        val statFs = getStats(external)
        val availableBlocks = statFs.availableBlocks.toLong()
        val blockSize = statFs.blockSize.toLong()
        val freeBytes = availableBlocks * blockSize
        return (freeBytes / MEGA_BYTE).toInt()
    }

    /**
     * Calculates occupied space on disk
     * @param external  If true will query external disk, otherwise will query internal disk.
     * @return Number of occupied mega bytes on disk.
     */
    fun busySpace(external: Boolean): Int {
        val statFs = getStats(external)
        val total = (statFs.blockCount * statFs.blockSize).toLong()
        val free = (statFs.availableBlocks * statFs.blockSize).toLong()
        return ((total - free) / MEGA_BYTE).toInt()
    }

    private fun getStats(external: Boolean): StatFs {
        val path: String
        path = if (external) {
            Environment.getExternalStorageDirectory().absolutePath
        } else {
            Environment.getRootDirectory().absolutePath
        }
        return StatFs(path)
    }
}