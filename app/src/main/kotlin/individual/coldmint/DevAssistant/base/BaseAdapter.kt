package individual.coldmint.DevAssistant.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 基础适配器
 */
abstract class BaseAdapter<DataType, ViewBindingType : ViewBinding>(
    val context: Context,
    val dataList: MutableList<DataType>
) : RecyclerView.Adapter<BaseAdapter.ViewHolder<ViewBindingType>>() {

    protected val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private var listEventLoader: ListEventLoader<DataType, ViewBindingType>? = null

    /**
     * 设置列表事件加载器
     * @param func Function4<Int, Int, ItemToolBinding, ViewHolder<ItemToolBinding>, Unit>?
     */
    fun setListEventLoader(func: ((Int, DataType, ViewBindingType, ViewHolder<ViewBindingType>) -> Unit)?) {
        listEventLoader = if (func == null) {
            null
        } else {
            object : ListEventLoader<DataType, ViewBindingType> {
                override fun loadEvent(
                    position: Int,
                    data: DataType,
                    viewBinding: ViewBindingType,
                    viewHolder: ViewHolder<ViewBindingType>
                ) {
                    func.invoke(position, data, viewBinding, viewHolder)
                }

            }
        }
    }

    abstract fun getViewBindingObject(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBindingType

    /**
     * 当绑定项目时
     * @param position Int
     * @param data DataType
     * @param viewBinding ViewBindingType
     * @param viewHolder ViewHolder<ViewBindingType>
     */
    abstract fun onBindItem(
        position: Int,
        data: DataType,
        viewBinding: ViewBindingType,
        viewHolder: ViewHolder<ViewBindingType>
    )

    class ViewHolder<ViewBindingType : ViewBinding>(val viewBinding: ViewBindingType) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ViewBindingType> {
        return ViewHolder(getViewBindingObject(layoutInflater, parent))
    }

    override fun onBindViewHolder(holder: ViewHolder<ViewBindingType>, position: Int) {
        val data = dataList[position]
        val viewBinding = holder.viewBinding
        onBindItem(position, data, viewBinding, holder)
        listEventLoader?.loadEvent(position, data, viewBinding, holder)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}