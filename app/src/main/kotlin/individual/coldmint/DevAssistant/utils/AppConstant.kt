package individual.coldmint.DevAssistant.utils

import android.graphics.Color

/**
 * 常量类
 */
object AppConstant {

    //文件保存目录（外部）
    private const val fileStorageDirectory = "/storage/emulated/0/developmentToolBox/"

//    private const val outputDirectory = fileStorageDirectory + "output/"

    //Dimens输出目录
    const val dimensOutPutDirectory = fileStorageDirectory + "output/dimens/"
    //颜色转换器输入错误时的图标颜色
    const val inputErrorColor = Color.RED


}