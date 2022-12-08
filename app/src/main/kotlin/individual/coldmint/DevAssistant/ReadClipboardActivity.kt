package individual.coldmint.DevAssistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import individual.coldmint.DevAssistant.base.BaseActivity
import individual.coldmint.DevAssistant.databinding.ActivityReadClipboardBinding
import individual.coldmint.DevAssistant.viewModels.ColorConversionViewModel
import individual.coldmint.DevAssistant.viewModels.ReadClipboardViewModel

class ReadClipboardActivity : BaseActivity<ActivityReadClipboardBinding>() {
    private val viewModel: ReadClipboardViewModel by lazy {
        ViewModelProvider(this).get(ReadClipboardViewModel::class.java)
    }

    override fun getViewBindingObject(layoutInflater: LayoutInflater): ActivityReadClipboardBinding {
        return ActivityReadClipboardBinding.inflate(layoutInflater);
    }

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        title = getString(R.string.clipboard)
        setReturnButton()
        viewBinding.readClipboardButton.setOnClickListener {
            val text = viewModel.readClipboard(this)
            if (text == null) {
                Snackbar.make(
                    viewBinding.readClipboardButton,
                    R.string.read_clipboard_fail,
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                viewBinding.editTextView.setText(text)
            }
        }
    }

}