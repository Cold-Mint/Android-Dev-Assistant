package individual.coldmint.DevAssistant.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import individual.coldmint.DevAssistant.ColorConversionActivity
import individual.coldmint.DevAssistant.DimensFileGenerationActivity
import individual.coldmint.DevAssistant.R
import individual.coldmint.DevAssistant.ReadClipboardActivity
import individual.coldmint.DevAssistant.adapters.ToolsAdapter
import individual.coldmint.DevAssistant.base.BaseFragment
import individual.coldmint.DevAssistant.databinding.FragmentToolsBinding

/**
 * @Author: test
 * 包名: individual.coldmint.developmenttoolbox.fragment
 * 类名: ToolsFragment
 * 版本: 1.0
 * 创建时间: 2022/10/8 17:11
 * 项目名称: Development Toolbox
 * 更新作者:
 * 更新时间:
 * 描述:
 */
class ToolsFragment : BaseFragment<FragmentToolsBinding>() {
    override fun getViewBindingObject(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentToolsBinding {
        return FragmentToolsBinding.inflate(inflater, container, false)
    }

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        initView()
    }

    fun initView() {
        val dataList = ArrayList<Int>()
        dataList.add(R.string.dimens_file_generation)
        dataList.add(R.string.color_conversion)
        dataList.add(R.string.clipboard)
        val adapter = ToolsAdapter(requireContext(), dataList)
        adapter.setListEventLoader { _, i2, itemToolBinding, _ ->
            itemToolBinding.root.setOnClickListener {
                if (i2 == R.string.dimens_file_generation) {
                    val intent = Intent(requireContext(), DimensFileGenerationActivity::class.java)
                    startActivity(intent)
                } else if (i2 == R.string.color_conversion) {
                    val intent = Intent(requireContext(), ColorConversionActivity::class.java)
                    startActivity(intent)
                } else if (i2 == R.string.clipboard) {
                    val intent = Intent(requireContext(), ReadClipboardActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        viewBinding.recyclerView.adapter = adapter
    }
}