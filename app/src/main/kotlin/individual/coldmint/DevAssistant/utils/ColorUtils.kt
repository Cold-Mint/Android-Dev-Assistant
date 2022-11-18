package individual.coldmint.DevAssistant.utils

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * 颜色转换类
 */
object ColorUtils {

    /**
     * 将颜色转换为String
     * @param color Int 颜色
     * @param alpha Boolean 透明度（默认不转换）
     * @param addSymbol Boolean （添加井号）
     * @return String
     */
    fun colorToString(
        @ColorInt color: Int,
        alpha: Boolean = false,
        addSymbol: Boolean = false
    ): String {
        // 转化为16进制字符串

        var A: String? = null
        var R = Integer.toHexString(Color.red(color))
        var G = Integer.toHexString(Color.green(color))
        var B = Integer.toHexString(Color.blue(color))

        // 判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        if (alpha) {
            A = Integer.toHexString(Color.alpha(color))
            A = if (A.length == 1) "0$A" else A
        }
        R = if (R.length == 1) "0$R" else R
        G = if (G.length == 1) "0$G" else G
        B = if (B.length == 1) "0$B" else B
        //
        val sb = StringBuffer()
        if (addSymbol) {
            sb.append("#")
        }
        if (alpha) {
            sb.append(A)
        }
        sb.append(R)
        sb.append(G)
        sb.append(B)
        return sb.toString()
    }

    /**
     * 将字符串 #AARRGGBB 转化为 int
     *
     *  * `#RRGGBB`
     *  * `#AARRGGBB`
     *
     *
     * @param AARRGGBB
     * @return
     */
    @ColorInt
    fun parseColor(string: String): Int {
        return try {
            if (string.isBlank()) {
                AppConstant.inputErrorColor
            } else {
                Color.parseColor(string)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AppConstant.inputErrorColor
        }
    }
}