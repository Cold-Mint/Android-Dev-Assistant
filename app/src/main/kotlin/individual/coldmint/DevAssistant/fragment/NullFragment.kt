package individual.coldmint.DevAssistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import individual.coldmint.DevAssistant.base.BaseFragment
import individual.coldmint.DevAssistant.databinding.FragmentNullBinding

/**
 * 空白碎片
 */
class NullFragment : BaseFragment<FragmentNullBinding>() {
    override fun getViewBindingObject(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNullBinding {
        return FragmentNullBinding.inflate(inflater,container,false)
    }

    override fun onCreateFragment(savedInstanceState: Bundle?) {
    }
}