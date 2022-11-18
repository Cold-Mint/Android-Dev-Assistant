package individual.coldmint.DevAssistant

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModelProvider
import individual.coldmint.DevAssistant.base.BaseActivity
import individual.coldmint.DevAssistant.databinding.ActivityColorConversionBinding
import individual.coldmint.DevAssistant.dialogs.ColorPickerDialog
import individual.coldmint.DevAssistant.utils.AppConstant
import individual.coldmint.DevAssistant.utils.ColorUtils
import individual.coldmint.DevAssistant.viewModels.ColorConversionViewModel

class ColorConversionActivity : BaseActivity<ActivityColorConversionBinding>() {

    private val viewModel: ColorConversionViewModel by lazy {
        ViewModelProvider(this).get(ColorConversionViewModel::class.java)
    }

    override fun getViewBindingObject(layoutInflater: LayoutInflater): ActivityColorConversionBinding {
        return ActivityColorConversionBinding.inflate(layoutInflater)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateActivity(savedInstanceState: Bundle?) {
        title = getString(R.string.color_conversion)
        setReturnButton()
        loadObserve()
        loadAction()
    }

    /**
     * 加载观察者
     */
    fun loadObserve() {
        viewModel.getColourData().observe(this) {
            viewBinding.colorEditText.setText(ColorUtils.colorToString(it))
            setAlpha(Color.alpha(it))
            setEndIconColor(it)
        }
    }

    /**
     * 加载活动
     */
    fun loadAction() {
        viewBinding.colorContentLayout.setEndIconOnClickListener {
            ColorPickerDialog(this).show { s, i ->
                viewModel.getColourData().postValue(i)
                true
            }
        }
        viewBinding.colorEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()
                viewModel.getColourData().value = if (str.isBlank()) {
                    AppConstant.inputErrorColor
                } else {
                    ColorUtils.parseColor("#" + s.toString())
                }
            }

        })
    }


    /**
     * 设置透明度
     * @param color Int
     */
    fun setAlpha(@ColorInt color: Int) {
        val alpha = Color.alpha(color)
        viewBinding.transparencyEditText.setText(alpha.toString())
    }

    /**
     * 设置结束图标颜色
     */
    fun setEndIconColor(@ColorInt color: Int) {
        val s = IntArray(1)
        s[0] = 0
        val c = IntArray(1)
        c[0] = color
        viewBinding.colorContentLayout.setEndIconTintList(
            ColorStateList(
                arrayOf(s),
                c
            )
        )
    }

}