package individual.coldmint.DevAssistant.viewModels

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import individual.coldmint.DevAssistant.utils.AppConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * 布局文件生成ViewModel
 */
class DimensFileGenerationViewModel : ViewModel() {


    //缓存文件路径
    private var cacheFilePath: String? = null

    /**
     * 获取屏幕Dpi
     * @param activity [Error type: Unresolved type for Activity]
     * @return Int
     */
    fun getDensityDPI(activity: Activity): Int {
        var displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.densityDpi
    }

    /**
     * 获取缓存文件路径
     * @param context Context
     * @return String
     */
    fun getCacheFilePath(context: Context): String {
        if (cacheFilePath == null) {
            cacheFilePath = context.cacheDir.absolutePath + "/temporary_dimens.xml"
            //如果是第一次获取，那么删除历史文件
            File(cacheFilePath).delete()
        }
        return cacheFilePath!!
    }

    /**
     * 输入一个项目返回其对应的values文件夹名
     * @param item String
     * @return 文件夹名
     */
    fun generateChipText(item: String): String? {
        val symbolIndex = item.indexOf('-')
        if (symbolIndex > -1) {
            //指定了文件夹名
            val name = item.subSequence(symbolIndex, item.length)
            val num = item.subSequence(0, symbolIndex).toString().toInt()
            val density = getDensity(num)
            return "values${name}(${density})"
        } else {
            //未指定文件夹名
            try {
                val num = item.toInt()
                val density = getDensity(num)
                return if (num <= 120) {
                    "values-ldpi(${density})"
                } else if (num <= 160) {
                    "values-mdpi(${density})"
                } else if (num <= 240) {
                    "values-hdpi(${density})"
                } else if (num <= 320) {
                    "values-xhdpi(${density})"
                } else if (num <= 480) {
                    "values-xxhdpi(${density})"
                } else if (num <= 640) {
                    "values-xxxhdpi(${density})"
                } else {
                    "values(${density})"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }
    }

    /**
     * 输入一个项目返回其对应的values文件夹名
     * @param item String
     * @return 文件夹名
     */
    fun generateFolderName(item: String): String? {
        val symbolIndex = item.indexOf('-')
        if (symbolIndex > -1) {
            //指定了文件夹名
            val name = item.subSequence(symbolIndex, item.length)
            return "values" + name
        } else {
            //未指定文件夹名
            try {
                val num = item.toInt()
                return if (num <= 120) {
                    "values-ldpi"
                } else if (num <= 160) {
                    "values-mdpi"
                } else if (num <= 240) {
                    "values-hdpi"
                } else if (num <= 320) {
                    "values-xhdpi"
                } else if (num <= 480) {
                    "values-xxhdpi"
                } else if (num <= 640) {
                    "values-xxxhdpi"
                } else {
                    "values"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }
    }

    /**
     * 写入缓存文件
     * @param context Context
     * @param uri Uri
     * @param parsingCallback Function1<Boolean, Unit>
     */
    fun writingToCacheFile(context: Context, uri: Uri, parsingCallback: (Boolean) -> Unit) {
        val job = Job()
        val handler = Handler(Looper.getMainLooper())
        CoroutineScope(job).launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val parcelFileDescriptor = context.contentResolver.openFile(uri, "r", null)
                val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
                val outputStream = FileOutputStream(getCacheFilePath(context))
                outputStream.write(inputStream.readBytes())
                outputStream.close()
                handler.post {
                    parsingCallback.invoke(true)
                }
            } else {
                try {
                    val file = uri.toFile()
                    val inputStream = file.inputStream()
                    val outputStream = FileOutputStream(getCacheFilePath(context))
                    outputStream.write(inputStream.readBytes())
                    outputStream.close()
                    handler.post {
                        parsingCallback.invoke(true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    handler.post {
                        parsingCallback.invoke(false)
                    }
                }
            }
        }
    }

    /**
     * 获取屏幕密度
     * @param dpi Int
     * @return Float
     */
    fun getDensity(dpi: Int): Float {
        return (dpi / 160.0).toFloat()
    }

    /**
     * 输出多个文件
     * @param context Context
     * @param targetDpi String
     * @param oldDensity Float
     * @param callBack Function3<Int, Int, String, Unit>
     */
    fun outputFile(
        context: Context, targetDpi: String, oldDensity: Float,
        callBack: (
            Int,
            Int,
            String, String
        ) -> Unit
    ) {
        val job = Job()
        val handler = Handler(Looper.getMainLooper())
        CoroutineScope(job).launch {
            val errorMessage = StringBuilder()
            var successNum = 0
            var totalNum = 0
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd_hh:mm:ss")
            val date = simpleDateFormat.format(Date())
            val folderPath = AppConstant.dimensOutPutDirectory + date
            File(folderPath).mkdirs()
            if (targetDpi.isNotBlank()) {
                targetDpi.split(',').forEach {
                    if (it.isNotBlank()) {
                        totalNum += 1
                        val value = it.trim()
                        val folderName = generateFolderName(value) ?: return@forEach
                        val symbol = value.indexOf('-')
                        val num: String = if (symbol > -1) {
                            value.subSequence(0, symbol).toString()
                        } else {
                            value
                        }
                        val newDensity = getDensity(num.toInt())
                        val newFolderPath = "$folderPath/${folderName}"
                        val newFolder = File(newFolderPath)
                        newFolder.mkdirs()
                        val newFile = "$newFolderPath/dimens.xml"
                        val finalInputStream =
                            FileInputStream(getCacheFilePath(context))
                        val b = calculateDimens(
                            finalInputStream,
                            oldDensity,
                            newDensity,
                            newFile, errorMessage
                        )
                        if (b) {
                            successNum += 1
                        }
                    }
                }
            }
            handler.post {
                callBack.invoke(totalNum, successNum, folderPath, errorMessage.toString())
            }
        }
    }


    /**
     * 计算新的尺寸文件（堵塞线程）
     * @param inputStream InputStream 输入流
     * @param oldDensity Float 旧的分辨率
     * @param newDensity Float 新的分辨率
     * @param newFilePath String 新文件路径
     */
    private fun calculateDimens(
        inputStream: InputStream,
        oldDensity: Float,
        newDensity: Float,
        newFilePath: String,
        errorMessage: StringBuilder
    ): Boolean {
        val factory = DocumentBuilderFactory.newInstance()
        try {
            val format = DecimalFormat("0.00")
            val documentBuilder = factory.newDocumentBuilder()
            val document = documentBuilder.parse(inputStream)
            // 所有的dimen节点
            val dimenList = document.getElementsByTagName("dimen")
            for (i in 0 until dimenList.length) {
                val dimenItem = dimenList.item(i).firstChild
                // 拿到值，并计算新值
                val nodeValue = dimenItem.nodeValue
                if (!TextUtils.isEmpty(nodeValue)) {
                    val value = nodeValue.substring(0, nodeValue.length - 2)
                    val numValue = value.toFloat()
                    // 保留两位小数
                    val formatStr: String =
                        format.format((Math.round(numValue * oldDensity) / newDensity).toDouble())
                    val newNumValue = Math.abs(formatStr.toFloat())
                    var result = newNumValue.toString()
                    if (newNumValue % 1 == 0f) {
                        // 整数不显示小数点后的0
                        result = newNumValue.toInt().toString()
                    }
                    // 设置拼接的新值
                    dimenItem.nodeValue = result + nodeValue.substring(nodeValue.length - 2)
                }
            }
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()
            val source = DOMSource(document)
            val result: StreamResult = StreamResult(File(newFilePath))
            transformer.transform(source, result)
            Log.d("转换", "run: dimen转换完成")
            return true
        } catch (e: Exception) {
            Log.e("转换", "run: dimen转换异常：$e")
            if (errorMessage.isNotEmpty()) {
                errorMessage.append('\n')
            }
            errorMessage.append(e)
            e.printStackTrace()
            return false
        }
    }
}