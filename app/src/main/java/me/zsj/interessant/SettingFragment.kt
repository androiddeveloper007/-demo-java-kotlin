@file:Suppress("DEPRECATION")

package me.zsj.interessant

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import me.zsj.interessant.utils.PreferenceManager
import java.io.File
import java.util.*

class SettingFragment : PreferenceFragment(), Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener {

    companion object {
        const val CACHE_KEY = "cache_with_wifi"
    }

    private var context: Activity? = null
    private var cacheVideoFile: File? = null
    private var clearVideoCache: Preference? = null
    private var cacheWithWifi: SwitchPreference? = null
    private var clearPhotoCache: Preference? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.context = context as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        cacheWithWifi = findPreference("cache_with_wifi") as SwitchPreference
        cacheWithWifi!!.onPreferenceChangeListener = this
        val checked = PreferenceManager.getBooleanValue(context, CACHE_KEY, true)
        cacheWithWifi!!.isChecked = checked
        clearVideoCache = findPreference("clear_video_cache")
        clearVideoCache!!.onPreferenceClickListener = this
        val videoFileSize = calculateCacheFileSize("video-cache", true)
        clearVideoCache!!.summary = String.format(Locale.CHINESE, "%.2fMb", videoFileSize)
        clearPhotoCache = findPreference("clear_photo_cache")
        clearPhotoCache!!.onPreferenceClickListener = this
        var photoFileSize = calculateCacheFileSize("image_manager_disk_cache", false)
        photoFileSize += calculateCacheFileSize("picasso-cache", false)
        clearPhotoCache!!.summary = String.format(Locale.CHINESE, "%.2fMb", photoFileSize)
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        if ("clear_video_cache".equals(preference?.key)) {
            deleteCacheFiles(cacheVideoFile, true);
        } else if ("clear_photo_cache".equals(preference?.key)) {
            deleteCacheFiles(context!!.getCacheDir(), false);
        }
        return false;
    }

    override fun onPreferenceChange(p0: Preference?, newValue: Any?): Boolean {
        val checked = newValue as Boolean
        cacheWithWifi!!.isChecked = checked
        PreferenceManager.putBoolean(context, CACHE_KEY, checked)
        return false
    }

    private fun calculateCacheFileSize(cachePath: String, videoPath: Boolean): Float {
        val cacheFile = if (videoPath) context!!.externalCacheDir else context!!.cacheDir
        var videoCacheFile: File? = null
        assert(cacheFile != null)
        for (file in cacheFile!!.listFiles()) {
            if (cachePath == file.name) {
                videoCacheFile = file
                break
            }
        }
        if (videoCacheFile != null && videoPath) {
            this.cacheVideoFile = videoCacheFile
        }
        var totalSize = 0f  //Mb
        if (videoCacheFile != null && videoCacheFile.isDirectory) {
            for (file in videoCacheFile.listFiles()) {
                totalSize += file.length().toFloat() / (1024 * 1024).toFloat()
            }
        }
        return totalSize
    }

    fun deleteCacheFiles(cacheFile: File?, videoPath: Boolean) {
        if (cacheFile != null && cacheFile.isDirectory()) {
            for (file in cacheFile.listFiles()) {
                if (file.isDirectory()) {
                    clearImageCache(file);
                } else if (file.isFile()) {
                    file.delete();
                }
            }
            if (videoPath && cacheFile.listFiles().isEmpty()) {
                clearVideoCache!!.setSummary("0.00Mb");
            }
        }
    }

    private fun clearImageCache(cacheImageFile: File) {
        for (file in cacheImageFile.listFiles()) {
            if (file.isFile) {
                file.delete()
            }
        }
        if (cacheImageFile.listFiles().size == 0) {
            clearPhotoCache!!.summary = "0.00Mb"
        }
    }
}