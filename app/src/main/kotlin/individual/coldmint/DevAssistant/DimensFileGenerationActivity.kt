package individual.coldmint.DevAssistant

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import individual.coldmint.DevAssistant.base.BaseActivity
import individual.coldmint.DevAssistant.databinding.ActivityDimensFileGenerationBinding
import individual.coldmint.DevAssistant.viewModels.DimensFileGenerationViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX
import java.io.*
import java.util.*

class DimensFileGenerationActivity : BaseActivity<ActivityDimensFileGenerationBinding>() {

    private lateinit var selectFile: ActivityResultLauncher<String>

    val viewModel: DimensFileGenerationViewModel by lazy {
        ViewModelProvider(this).get(DimensFileGenerationViewModel::class.java)
    }

    override fun getViewBindingObject(layoutInflater: LayoutInflater): ActivityDimensFileGenerationBinding {
        return ActivityDimensFileGenerationBinding.inflate(layoutInflater)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dimens_file_generation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_create) {
            MaterialAlertDialogBuilder(this@DimensFileGenerationActivity).setTitle(R.string.dimens_file_generation_create_title)
                .setMessage(
                    R.string.dimens_file_generation_create_describe
                ).setPositiveButton(
                    R.string.confirm
                ) { dialog, which ->
                    val text = viewBinding.targetDpiEditText.text.toString()
                    val newText =
                        "$text ${viewModel.getDensityDPI(this)}-sw${resources.configuration.smallestScreenWidthDp}dp,"
                    viewBinding.targetDpiEditText.setText(newText)
                }.setNegativeButton(
                    R.string.cancel
                ) { dialog, which -> }.setCancelable(false).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("PrivateResource")
    override fun onCreateActivity(savedInstanceState: Bundle?) {
        title = getString(R.string.dimens_file_generation)
        setReturnButton()
        selectFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) {
                return@registerForActivityResult
            }
            viewModel.writingToCacheFile(this, uri) {
                if (it) {
                    viewBinding.sourceContentEditText.setText(uri.toString())
                    Snackbar.make(
                        viewBinding.fab,
                        R.string.file_parsing_succeeded,
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        viewBinding.fab,
                        R.string.file_parsing_failed,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        loadView()
        loadAction()
    }


    fun loadView() {
        viewBinding.sourceContentLayout.setEndIconOnClickListener {
            selectFile.launch("*/*")
        }

        val dpiList = arrayOf<String>("120", "160", "240", "320", "480", "640")
        viewBinding.dpiEditText.setSimpleItems(dpiList)

        val targetDpiList = mutableListOf<String>("120", "160", "240", "320", "480", "640")
        viewBinding.targetDpiEditText.threshold = 1
        viewBinding.targetDpiEditText.setAdapter(
            ArrayAdapter(
                this,
                com.google.android.material.R.layout.m3_auto_complete_simple_item, targetDpiList
            )
        )
        viewBinding.targetDpiEditText.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
    }

    fun loadAction() {
        viewBinding.dpiEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.isBlank()) {
                    viewBinding.dpiLayout.helperText = ""
                } else {
                    viewBinding.dpiLayout.helperText = viewModel.generateChipText(input)
                }
            }
        })
        viewBinding.targetDpiEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewBinding.chipGroup.removeAllViews()
                val input = s.toString()
                if (input.isNotBlank()) {
                    input.split(',').forEach {
                        if (it.isNotBlank()) {
                            val name = viewModel.generateChipText(it.trim())
                            if (name != null) {
                                val chip = Chip(this@DimensFileGenerationActivity)
                                chip.text = name
                                viewBinding.chipGroup.addView(chip)
                            }
                        }
                    }
                }
            }

        })

        viewBinding.fab.setOnClickListener {
            if (!File(viewModel.getCacheFilePath(this)).exists()) {
                Snackbar.make(
                    viewBinding.fab,
                    R.string.please_select_the_file_first,
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val oldDpi = viewBinding.dpiEditText.text.toString()
            if (oldDpi.isBlank()) {
                Snackbar.make(
                    viewBinding.fab,
                    R.string.please_enter_the_dpi,
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val oldDensity = viewModel.getDensity(oldDpi.toInt())
            val targetDpi = viewBinding.targetDpiEditText.text.toString()
            if (targetDpi.isBlank()) {
                Snackbar.make(
                    viewBinding.fab,
                    R.string.please_enter_the_target_dpi,
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
//申请权限
            PermissionX.init(this).permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ).request { allGranted, grantedList, deniedList ->
                viewModel.outputFile(this, targetDpi, oldDensity) { totalNum,
                                                                    successNum,
                                                                    folderPath, errorMessage ->
                    val dialog =
                        MaterialAlertDialogBuilder(this@DimensFileGenerationActivity).setTitle(R.string.dimens_file_generation)
                            .setMessage(
                                String.format(
                                    getString(R.string.dimens_file_generation_complete_tip),
                                    totalNum,
                                    successNum,
                                    folderPath
                                )
                            ).setPositiveButton(
                                R.string.confirm
                            ) { dialog, which -> }.setCancelable(false)
                    if (totalNum > successNum) {
                        //如果有处理错误
                        dialog.setNegativeButton(R.string.error_details) { dialog, which ->
                            //弹出错误详情提示
                            MaterialAlertDialogBuilder(this@DimensFileGenerationActivity).setTitle(R.string.error_details)
                                .setMessage(
                                    errorMessage
                                ).setPositiveButton(
                                    R.string.confirm
                                ) { dialog, which -> }.setCancelable(false).show()

                        }
                    }
                    dialog.show()
                }
            }


        }

    }


}