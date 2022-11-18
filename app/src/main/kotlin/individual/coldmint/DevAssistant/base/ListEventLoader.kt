package individual.coldmint.DevAssistant.base

import androidx.viewbinding.ViewBinding

/**
 * 列表事件加载器
 */
interface ListEventLoader<DataType, ViewBindingType : ViewBinding> {

    /**
     * 当加载事件时
     * @param position Int
     * @param data DataType
     * @param viewBinding ViewBindingType
     * @param viewHolder ViewHolder<ViewBindingType>
     */
    fun loadEvent(
        position: Int,
        data: DataType,
        viewBinding: ViewBindingType,
        viewHolder: BaseAdapter.ViewHolder<ViewBindingType>
    )

}