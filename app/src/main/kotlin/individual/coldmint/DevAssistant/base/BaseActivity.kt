package individual.coldmint.DevAssistant.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import individual.coldmint.DevAssistant.R
import com.google.android.material.appbar.MaterialToolbar

/**
 * 基类
 */
abstract class BaseActivity<ViewBindingType : ViewBinding> : AppCompatActivity() {

    protected lateinit var viewBinding: ViewBindingType

    /**
     * 设置返回按钮
     */
    fun setReturnButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 获取布局绑定对象
     */
    abstract fun getViewBindingObject(layoutInflater: LayoutInflater): ViewBindingType

    /**
     * 当创建Activity
     */
    abstract fun onCreateActivity(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        viewBinding = getViewBindingObject(layoutInflater)
        setContentView(viewBinding.root)
        val toolbar: MaterialToolbar? = findViewById(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        onCreateActivity(savedInstanceState)
    }
}