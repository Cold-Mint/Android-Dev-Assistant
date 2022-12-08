package individual.coldmint.DevAssistant.viewModels

import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel

class ReadClipboardViewModel : ViewModel() {


    /**
     * 读取剪切板
     */
    fun readClipboard(context: Context): String? {
        val clipboardManager: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboardManager == null) {
            return null
        } else {
            val clipData = clipboardManager.primaryClip ?: return null
            if (clipboardManager.hasPrimaryClip() && clipData.itemCount > 0){
                val addText = clipData.getItemAt(0).text
                return addText.toString()
            }else{
                return null
            }
        }
    }

}