package individual.coldmint.DevAssistant.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import individual.coldmint.DevAssistant.databinding.DialogColorPickerBinding
import individual.coldmint.DevAssistant.utils.ColorUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * 颜色选择对话框
 */
class ColorPickerDialog(private val context: Context) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private val viewBinding: DialogColorPickerBinding by lazy {
        DialogColorPickerBinding.inflate(layoutInflater)
    }

    private val dialogBuilder: MaterialAlertDialogBuilder by lazy {
        MaterialAlertDialogBuilder(context)
    }

    private var dialog: AlertDialog? = null

    private var callBack: ((String, Int) -> Boolean)? = null

    init {
        dialogBuilder.setView(viewBinding.root)
        viewBinding.cancelButton.setOnClickListener {
            dialog?.dismiss()
        }

        viewBinding.confirmButton.setOnClickListener {
            val color = viewBinding.colorPickerView.selectedColor
            val colorStr = ColorUtils.colorToString(color,true)
            val close = callBack?.invoke(colorStr, color) ?: false
            if (close) {
                dialog?.dismiss()
            }
        }
    }


    /**
     * 显示对话框
     * @param callBack Function2<String, Int, Boolean>? 回调,返回true即可关闭对话框
     */
    fun show(callBack: ((String, Int) -> Boolean)? = null) {
        this.callBack = callBack
        dialog = dialogBuilder.show()
    }


}