package individual.coldmint.DevAssistant.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Author: test
 * 包名: individual.coldmint.developmenttoolbox.viewModels
 * 类名: ColorConversionViewModel
 * 版本: 1.0
 * 创建时间: 2022/10/25 14:19
 * 项目名称: Development Toolbox
 * 更新作者:
 * 更新时间:
 * 描述:
 */
class ColorConversionViewModel : ViewModel() {


    /**
     * 颜色数据
     */
    private val colourLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    /**
     * 获取颜色数据对象
     * @return MutableLiveData<Int>
     */
    fun getColourData(): MutableLiveData<Int> {
        return colourLiveData
    }


}