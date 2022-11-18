package individual.coldmint.DevAssistant.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import individual.coldmint.DevAssistant.base.BaseAdapter
import individual.coldmint.DevAssistant.databinding.ItemToolBinding

/**
 * @Author: test
 * 包名: individual.coldmint.developmenttoolbox.adapters
 * 类名: ToolsAdapter
 * 版本: 1.0
 * 创建时间: 2022/10/8 17:22
 * 项目名称: Development Toolbox
 * 更新作者:
 * 更新时间:
 * 描述:
 */
class ToolsAdapter(context: Context, dataList: MutableList<Int>) :
    BaseAdapter<Int, ItemToolBinding>(context, dataList) {



    override fun onBindItem(
        position: Int,
        data: Int,
        viewBinding: ItemToolBinding,
        viewHolder: ViewHolder<ItemToolBinding>
    ) {
        viewBinding.titleView.setText(data)
    }

    override fun getViewBindingObject(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ItemToolBinding {
        return ItemToolBinding.inflate(layoutInflater, parent, false)
    }
}