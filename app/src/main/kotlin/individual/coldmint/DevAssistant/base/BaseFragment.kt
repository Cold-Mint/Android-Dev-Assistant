package individual.coldmint.DevAssistant.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * 主要的碎片
 */
abstract class BaseFragment<ViewBindingType : ViewBinding> : Fragment() {

    protected lateinit var viewBinding: ViewBindingType

    abstract fun getViewBindingObject(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewBindingType

    abstract fun onCreateFragment(savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = getViewBindingObject(inflater, container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onCreateFragment(savedInstanceState)
    }
}