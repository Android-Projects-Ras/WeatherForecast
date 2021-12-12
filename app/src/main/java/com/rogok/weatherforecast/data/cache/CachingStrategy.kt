package com.rogok.weatherforecast.data.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface CachingStrategy {
    suspend fun loadBitmapAsync(iconCode: String): Bitmap
    suspend fun saveBitmap(bitmap: Bitmap, name: String): Uri
    suspend fun isImageExists(apiIconName: String): Boolean

    //suspend fun loadImageFromCache(cachedIconName: String)
    fun getIconName(iconCode: String): String
}

class CachingStrategyImpl(
    private val context: Context
) : CachingStrategy {

    private val cachePath = context.cacheDir.path

    /*override fun loadBitmap(iconCode: String): Bitmap {
        return try {
            val iconUrl = getIconUrl(iconCode)
            val url = URL(iconUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true //read data
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            throw e
            //null
        }
    }*/

    /**Чтобы все работало как надо, мы создаем новый поток с помощью Thread(),
     *  помещаем блокирующий код внутрь, и оборачиваем все это в блок suspendCoroutine { continuation ->}.
     *  Пока процесс выполняется в отдельном потоке, наша корутина «спит». Как только код закончит работу,
     *  мы передаем результат в continuation.resume().
     *  Тот в свою очередь возвращает данные в suspend функцию,
     *  а в случае ошибки вызываем continuation.resumeWithException() куда передаем наш Exceprion.*/

    override suspend fun loadBitmapAsync(iconCode: String): Bitmap {
        val iconUrl = getIconUrl(iconCode)
        return suspendCoroutine { continuation ->
            Thread(kotlinx.coroutines.Runnable {
                with(URL(iconUrl).openConnection() as HttpURLConnection) {
                    try {
                        continuation.resume(BitmapFactory.decodeStream(inputStream))
                    } catch (ex: Exception) {
                        continuation.resumeWithException(ex)
                    } finally {
                        disconnect()
                    }
                }
            }).start()
        }

    }

    override suspend fun saveBitmap(bitmap: Bitmap, name: String): Uri {
        //val fileName = "${name ?: UUID.randomUUID().toString().replace("-", "")}.png"
        val file = File(cachePath, name)
        //file.mkdir()
        //creates outputstream to write the file
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)//write to outputstream
        }
        return file.toUri()
    }

    override suspend fun isImageExists(apiIconName: String): Boolean {
        val filesList = File(cachePath).listFiles()
        if (filesList.isNullOrEmpty()) {
            return false
        } else {
            filesList.forEach {
                val cachedFileName = it.toString()
                if (apiIconName == cachedFileName) {
                    return true
                }
            }
        }
        return false
    }

    override fun getIconName(iconCode: String): String {
        val iconUrl = getIconUrl(iconCode)
        return iconUrl.substring(iconUrl.lastIndexOf("/") + 1)
    }

    private fun getIconUrl(iconCode: String): String =
        when (iconCode) {
            "01d" -> "https://openweathermap.org/img/wn/01d@2x.png"
            "01n" -> "https://openweathermap.org/img/wn/01n@2x.png"
            "02d" -> "https://openweathermap.org/img/wn/02d@2x.png"
            "02n" -> "https://openweathermap.org/img/wn/02n@2x.png"
            "03d" -> "https://openweathermap.org/img/wn/03d@2x.png"
            "03n" -> "https://openweathermap.org/img/wn/03n@2x.png"
            "04d" -> "https://openweathermap.org/img/wn/04d@2x.png"
            "04n" -> "https://openweathermap.org/img/wn/04n@2x.png"
            "09d" -> "https://openweathermap.org/img/wn/09d@2x.png"
            "09n" -> "https://openweathermap.org/img/wn/09n@2x.png"
            "10d" -> "https://openweathermap.org/img/wn/10d@2x.png"
            "10n" -> "https://openweathermap.org/img/wn/10n@2x.png"
            "11d" -> "https://openweathermap.org/img/wn/11d@2x.png"
            "11n" -> "https://openweathermap.org/img/wn/11n@2x.png"
            "13d" -> "https://openweathermap.org/img/wn/13d@2x.png"
            "13n" -> "https://openweathermap.org/img/wn/13n@2x.png"
            "50d" -> "https://openweathermap.org/img/wn/50d@2x.png"
            "50n" -> "https://openweathermap.org/img/wn/50n@2x.png"
            else -> "https://openweathermap.org/img/wn/01d@2x.png"
        }

}