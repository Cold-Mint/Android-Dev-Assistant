package individual.coldmint.DevAssistant.debug

import android.util.Log

/**
 * @Author: test
 * 包名: individual.coldmint.developmenttoolbox.debug
 * 类名: AppDebugger
 * 版本: 1.0
 * 创建时间: 2022/10/9 16:35
 * 项目名称: Development Toolbox
 * 更新作者:
 * 更新时间:
 * 描述:
 */
object AppDebugger {

    /**
     * 调试
     * @param tag String
     * @param msg String
     */
    fun debug(tag: String, msg: String) {
        Log.d(tag, msg)
    }

}